package group.assignment.booking_hotel_backend.services.impl;

import group.assignment.booking_hotel_backend.dto.*;
import group.assignment.booking_hotel_backend.mapper.BookingMapper;
import group.assignment.booking_hotel_backend.models.*;
import group.assignment.booking_hotel_backend.repository.*;
import group.assignment.booking_hotel_backend.services.BookingService;
import group.assignment.booking_hotel_backend.services.NotificationService;
import group.assignment.booking_hotel_backend.services.FirebaseMessagingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import group.assignment.booking_hotel_backend.exception.ResourceNotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final BillRepository billRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    @Autowired(required = false)
    private FirebaseMessagingService firebaseMessagingService;
    @Override
    public List<BookingSearchResponse> searchAvailableRooms(BookingSearchRequest request) {
        List<Hotel> hotels;

        if (request.getCity() != null && !request.getCity().isEmpty()
                && request.getDistrict() != null && !request.getDistrict().isEmpty()) {
            hotels = hotelRepository.findByAddressCityAndAddressDistrict(
                    request.getCity(), request.getDistrict()
            );
        } else {
            hotels = hotelRepository.findAll();
        }

        List<BookingSearchResponse> results = new ArrayList<>();

        LocalDateTime checkIn = parseDateTime(request.getCheckInDate(), request.getCheckInTime());
        LocalDateTime checkOut = parseDateTime(request.getCheckOutDate(), request.getCheckOutTime());

        for (Hotel hotel : hotels) {
            for (Room room : hotel.getRoomList()) {
                List<String> roomServiceNames = room.getServiceList().stream()
                        .map(Service::getServiceName)
                        .collect(Collectors.toList());

                String keyword = request.getInfoSearch();
                if (keyword != null && !keyword.trim().isEmpty()) {
                    String lowerKeyword = keyword.trim().toLowerCase();
                    String hotelName = hotel.getHotelName() != null ? hotel.getHotelName().toLowerCase() : "";
                    String roomName = room.getRoomName() != null ? room.getRoomName().toLowerCase() : "";
                    String roomDesc = room.getDescription() != null ? room.getDescription().toLowerCase() : "";
                    String city = hotel.getAddress().getCity() != null ? hotel.getAddress().getCity().toLowerCase() : "";
                    String district = hotel.getAddress().getDistrict() != null ? hotel.getAddress().getDistrict().toLowerCase() : "";
                    String ward = hotel.getAddress().getWard() != null ? hotel.getAddress().getWard().toLowerCase() : "";
                    String specific = hotel.getAddress().getSpecificAddress() != null ? hotel.getAddress().getSpecificAddress().toLowerCase() : "";

                    boolean match = hotelName.contains(lowerKeyword)
                            || roomName.contains(lowerKeyword)
                            || roomDesc.contains(lowerKeyword)
                            || city.contains(lowerKeyword)
                            || district.contains(lowerKeyword)
                            || ward.contains(lowerKeyword)
                            || specific.contains(lowerKeyword);

                    if (!match) continue;
                }

                if (request.getServices() != null && !roomServiceNames.containsAll(request.getServices())) {
                    continue;
                }

                if (request.getAdults() + request.getChildren() > room.getMaxOccupancy()) {
                    continue;
                }

                if (request.getBedNumber() != room.getBedNumber()) {
                    continue;
                }

                boolean isAvailable = isAvailable(room, checkIn, checkOut);
                if (!isAvailable) continue;

                double price = calculateTotalPrice(request, room);

                if (request.getPriceFrom() != null && price < request.getPriceFrom()) continue;
                if (request.getPriceTo() != null && price > request.getPriceTo()) continue;


                List<Booking> bookings = room.getBookingList();
                List<Review> allReviews = new ArrayList<>();
                for (Booking booking : bookings) {
                    if (booking.getReviewList() != null) {
                        allReviews.addAll(booking.getReviewList());
                    }
                }

                double avgRating = 0.0;
                if (!allReviews.isEmpty()) {
                    avgRating = allReviews.stream()
                            .filter(r -> r.getRating() != null)
                            .mapToInt(Review::getRating)
                            .average()
                            .orElse(0.0);
                }

                results.add(BookingSearchResponse.builder()
                        .roomId(room.getRoomId())
                        .roomName(room.getRoomName())
                        .price(price)
                        .hotelName(hotel.getHotelName())
                        .roomImg(room.getRoomImg())
                        .address(hotel.getAddress().getSpecificAddress())
                        .services(roomServiceNames)
                        .checkIn(formatDateTime(checkIn))
                        .checkOut(formatDateTime(checkOut))
                        .adults(request.getAdults())
                        .children(request.getChildren())
                        .bedNumber(request.getBedNumber())
                        .rating(avgRating)
                        .build());
            }
        }
        Comparator<BookingSearchResponse> comparator;

        switch (request.getSortBy()) {
            case "price_asc":
                comparator = Comparator.comparing(BookingSearchResponse::getPrice);
                break;
            case "price_desc":
                comparator = Comparator.comparing(BookingSearchResponse::getPrice).reversed();
                break;
            case "rating_asc":
                comparator = Comparator
                        .comparing(BookingSearchResponse::getRating)
                        .thenComparing(BookingSearchResponse::getPrice);
                break;
            case "rating_desc":
                comparator = Comparator
                        .comparing(BookingSearchResponse::getRating)
                        .reversed()
                        .thenComparing(BookingSearchResponse::getPrice);
                break;
            default:
                comparator = Comparator.comparing(BookingSearchResponse::getPrice);
                break;
        }

        results.sort(comparator);
        return results;
    }


    @Override
    public BookingResponseDto createBooking(BookingRequestDto request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        Bill bill = null;
        if (request.getBillId() != null) {
            bill = billRepository.findById(request.getBillId())
                    .orElseThrow(() -> new RuntimeException("Bill not found"));
        }

        if(bill == null){
            bill = Bill.builder()
                    .totalPrice(request.getPrice())
                    .paidStatus(false)
                    .user(user)
                    .build();
            bill = billRepository.save(bill);
            System.out.println("bill" + bill.getBillId());
        }
        System.out.println("bill" + bill.getBillId());

        Booking booking = Booking.builder()
                .checkIn(request.getCheckIn())
                .checkOut(request.getCheckOut())
                .price(request.getPrice())
                .status(BookingStatus.PENDING)
                .user(user)
                .room(room)
                .bill(bill)
                .build();

        Booking saved = bookingRepository.save(booking);
        return BookingMapper.mapToBookingResponseDto(saved, new BookingResponseDto());
    }

    @Override
    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

//    @Override
//    public BookingResponseDto findById(Integer id) {
//        Booking booking = bookingRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Booking not found"));
//        return BookingMapper.mapToBookingResponseDto(booking, new BookingResponseDto());
//    }

    @Override
    public Booking findById(Integer id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        return booking;
    }

    @Override
    public Booking updateBooking(Integer id, BookingRequestDto request) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setCheckIn(request.getCheckIn());
        booking.setCheckOut(request.getCheckOut());
        booking.setPrice(request.getPrice());

        if (request.getUserId() != null) {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            booking.setUser(user);
        }

        if (request.getRoomId() != null) {
            Room room = roomRepository.findById(request.getRoomId())
                    .orElseThrow(() -> new RuntimeException("Room not found"));
            booking.setRoom(room);
        }

        if (request.getBillId() != null) {
            Bill bill = billRepository.findById(request.getBillId())
                    .orElseThrow(() -> new RuntimeException("Bill not found"));
            booking.setBill(bill);
        }

        return bookingRepository.save(booking);
    }

    @Override
    public Booking update(Booking booking) {
        Booking existing = bookingRepository.findById(booking.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        existing.setCheckIn(booking.getCheckIn());
        existing.setCheckOut(booking.getCheckOut());
        existing.setPrice(booking.getPrice());
        existing.setStatus(booking.getStatus());
        if (booking.getUser() != null) {
            existing.setUser(booking.getUser());
        }
        if (booking.getRoom() != null) {
            existing.setRoom(booking.getRoom());
        }
        existing.setBill(booking.getBill());
        return bookingRepository.save(existing);
    }


    @Override
    public void deleteById(Integer id) {
        notificationService.deleteNotificationsByBookingId(id);
        bookingRepository.deleteById(id);
    }

    @Override
    public Booking updateBookingStatus(Integer bookingId, BookingStatus newStatus) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        BookingStatus oldStatus = booking.getStatus();
        booking.setStatus(newStatus);
        Booking updatedBooking = bookingRepository.save(booking);
        if (newStatus == BookingStatus.CANCELLED) {
            notificationService.handleBookingEvent(updatedBooking, "BOOKING_CANCEL");
        }
        if (newStatus == BookingStatus.CONFIRMED) {
            notificationService.handleBookingEvent(updatedBooking, "BOOKING_CONFIRM");
        }
        if (!oldStatus.equals(newStatus) && firebaseMessagingService != null) {
            try {
                firebaseMessagingService.sendBookingStatusUpdateNotification(updatedBooking);
            } catch (Exception e) {
                log.error("Failed to send booking status notification for booking {}: {}",
                        bookingId, e.getMessage());
            }
        }
        else {
            System.out.println("Firebase service is not available or status has not changed for booking " + bookingId);
            log.info("Firebase service is not available or status has not changed for booking {}", bookingId);
        }
        return updatedBooking;
    }

    @Override
    public List<Booking> findByUserId(Integer userId) {
        return bookingRepository.findByUserUserId(userId);
    }

    @Override
    public Long count() {
        return bookingRepository.count();
    }

    @Override
    public Map<String, Double> getDailyRevenueThisMonth() {
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1);

        List<Booking> bookings = bookingRepository
                .findByCheckInBetweenAndStatus(startOfMonth, endOfMonth, BookingStatus.CONFIRMED);

        Map<String, Double> dailyRevenue = new TreeMap<>();
        for (Booking booking : bookings) {
            String day = booking.getCheckIn().toLocalDate().toString();
            double price = booking.getPrice() != null ? booking.getPrice() : 0.0;
            dailyRevenue.put(day, dailyRevenue.getOrDefault(day, 0.0) + price);
        }
        return dailyRevenue;
    }

    @Override
    public Double getTotalRevenueBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return bookingRepository.getTotalRevenueBetweenDates(startDate, endDate);
    }



    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
        return dateTime.format(formatter);
    }

    private LocalDateTime parseDateTime(String date, String time) {
        String dateTimeString = date + " " + time;
        return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public boolean isAvailable(Room room, LocalDateTime checkIn, LocalDateTime checkOut) {
        List<Booking> existingBookings = bookingRepository
                .findByRoomRoomIdAndCheckOutAfterAndCheckInBefore(
                        room.getRoomId(), checkIn.minusHours(1), checkOut.plusHours(1)
                );
        for (Booking existingBooking : existingBookings) {
            if (!(checkOut.isBefore(existingBooking.getCheckIn()) || checkIn.isAfter(existingBooking.getCheckOut()))) {
                return false;
            }
        }
        return true;
    }

    private double calculateTotalPrice(BookingSearchRequest request, Room room) {
        Double pricePerNight = room.getPricePerNight();
        Double comboPriceFor2Hours = room.getComboPrice2h();
        Double extraHourRate = room.getExtraHourPrice();
        Double extraAdultRate = room.getExtraAdult();
        int freeChildrenCount = room.getNumChildrenFree();
        int standardOccupancy = room.getStandardOccupancy();

        Double extraCharges = 0D;
        long additionalPeopleCount = 0;

        if (request.getAdults() > standardOccupancy) {
            additionalPeopleCount = request.getAdults() - standardOccupancy;
        }
        if (request.getChildren() > freeChildrenCount) {
            additionalPeopleCount += request.getChildren() - freeChildrenCount;
        }

        extraCharges = additionalPeopleCount * extraAdultRate;
        String checkInDateTime = request.getCheckInDate() + " " + request.getCheckInTime() + ":00";
        String checkOutDateTime = request.getCheckOutDate() + " " + request.getCheckOutTime() + ":00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime checkInDateTimeParsed = LocalDateTime.parse(checkInDateTime, formatter);
        LocalDateTime checkOutDateTimeParsed = LocalDateTime.parse(checkOutDateTime, formatter);

        // gio check in check out tieu chuan
        LocalDateTime standardCheckInTime = LocalDateTime.of(checkInDateTimeParsed.toLocalDate(), LocalTime.of(14, 0));
        LocalDateTime standardCheckOutTime = LocalDateTime.of(checkOutDateTimeParsed.toLocalDate(), LocalTime.of(11, 0));

        // neu cung 1 ngay thi ngay check out la ngay hom sau
        if (checkInDateTimeParsed.toLocalDate().isEqual(checkOutDateTimeParsed.toLocalDate())) {
            standardCheckOutTime = standardCheckOutTime.plusDays(1);
        }
        // tinh thoi gian check in so voi thoi gian check in tieu chuan
        long earlyCheckInHours = 0L;
        if(checkInDateTimeParsed.isBefore(standardCheckInTime)) {
            earlyCheckInHours = ChronoUnit.HOURS.between(checkInDateTimeParsed, standardCheckInTime);
        }
        // tinh thoi gian check out so voi thoi gian check out tieu chuan
        long lateCheckOutHours = 0L;
        if(checkOutDateTimeParsed.isAfter(standardCheckOutTime)) {
            lateCheckOutHours = ChronoUnit.HOURS.between(standardCheckOutTime, checkOutDateTimeParsed);
        }

        long totalStayDurationInDays = ChronoUnit.DAYS.between(checkInDateTimeParsed, checkOutDateTimeParsed) + 1;
        double totalStayDays = (double) totalStayDurationInDays;
        double totalStayPrice = totalStayDays * pricePerNight
                + (earlyCheckInHours + lateCheckOutHours) * extraHourRate
                + extraCharges;

        // tinh tien theo gio
        long totalDurationInHours = ChronoUnit.HOURS.between(checkInDateTimeParsed, checkOutDateTimeParsed);
        Double timeBasedPrice = 0D;

        if (totalDurationInHours >= 2) {
            timeBasedPrice = comboPriceFor2Hours + extraHourRate * (totalDurationInHours - 2);
        } else if (totalDurationInHours < 2) {
            timeBasedPrice = comboPriceFor2Hours;
        }
        timeBasedPrice += extraCharges;
        return Math.min(totalStayPrice, timeBasedPrice);
    }

    @Override
    public AdminRevenueResponse getRevenueAndBookingDetails(LocalDateTime startDate, LocalDateTime endDate) {
        List<Booking> bookings = bookingRepository.findBookingsBetweenDates(startDate, endDate);
        List<AdminBookingDetailResponse> bookingDetails = new ArrayList<>();
        Double totalRevenue = 0.0;
        for (Booking booking : bookings) {
            if (booking.getStatus() == BookingStatus.CONFIRMED && booking.getBill().getPaidStatus()) {
                AdminBookingDetailResponse detailResponse = AdminBookingDetailResponse.builder()
                        .bookingId(booking.getBookingId())
                        .roomName(booking.getRoom().getRoomName())
                        .fullName(booking.getUser().getFullName())
                        .phone(booking.getUser().getPhone())
                        .checkIn(booking.getCheckIn())
                        .checkOut(booking.getCheckOut())
                        .price(booking.getPrice())
                        .createAt(booking.getCreatedAt())
                        .build();
                bookingDetails.add(detailResponse);
                totalRevenue += booking.getPrice();
            }
        }        return AdminRevenueResponse.builder()
                .bookingDetailList(bookingDetails)
                .totalRevenue(totalRevenue)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    @Override
    public List<BookingStatsDto> getBookingStatsLastNDaysForHotel(int hotelId, int days) {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(days);
 
        List<Object[]> results = bookingRepository.countBookingsPerDayForHotel(hotelId, startDate);
    
        Map<LocalDate, Long> bookingCountMap = results.stream()
            .collect(Collectors.toMap(
                row -> ((java.sql.Date) row[0]).toLocalDate(),
                row -> (Long) row[1]
            ));

        List<BookingStatsDto> statsForAllDays = new ArrayList<>();
        LocalDate date = startDate.toLocalDate();
        LocalDate endDateDay = endDate.toLocalDate();
        
        while (!date.isAfter(endDateDay)) {
            Long count = bookingCountMap.getOrDefault(date, 0L);
            statsForAllDays.add(new BookingStatsDto(date, count));
            date = date.plusDays(1);
        }
        
        return statsForAllDays;
    }

    @Override
    public List<Booking> findAllBookingsByHotelOwner(Integer userId) {
        return bookingRepository.findAllBookingsByHotelOwner(userId);
    }


    @Override
    public List<Booking> getCurrentBookingForHotel(int hotelId) {
       return bookingRepository.findConfirmedBookingsByHotelId(hotelId);
    }

    @Override
    public Page<Booking> getBookingsByHotelId(Integer hotelId, String query, Pageable pageable) {
        return bookingRepository.findByHotelIdAndQuery(hotelId, "%" + query.toLowerCase() + "%", pageable);
    }

    @Override
    public Long countBookingsByHotelId(Integer hotelId) {
        return bookingRepository.countByHotelId(hotelId);
    }

    @Override
    public boolean hasBookingsForRoom(Integer roomId) {
        // Kiểm tra booking với status là PENDING hoặc CONFIRMED
        List<BookingStatus> activeStatuses = Arrays.asList(
            BookingStatus.PENDING, 
            BookingStatus.CONFIRMED
        );
        
        List<Booking> bookings = bookingRepository.findByRoomIdAndStatusIn(roomId, activeStatuses);
        return !bookings.isEmpty();
    }

    @Override
    public List<BookingHotelOwnerDto> getAllBookingsByHotelId(
            Integer hotelId, Integer offset, Integer limit, String order, String query) {
        
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khách sạn"));
        
        Pageable pageable = createPageable(offset, limit, order);
        
        Page<Booking> bookings;
        
        if (query != null && !query.isEmpty()) {
            bookings = bookingRepository.findByHotelIdAndRoomNameContainingOrUserFullNameContaining(
                hotelId, query, pageable);
        } else {
            bookings = bookingRepository.findByHotelId(hotelId, pageable);
        }
        
        return bookings.stream()
                .map(this::convertToBookingResponseDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Lấy tất cả booking của một khách sạn với trạng thái cụ thể
     */
    @Override
    public List<BookingHotelOwnerDto> getAllBookingsByHotelIdAndStatus(
            Integer hotelId, Integer offset, Integer limit, String order, String query, BookingStatus status) {
        
       
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khách sạn"));
        

        Pageable pageable = createPageable(offset, limit, order);
        
  
        Page<Booking> bookings;
        
        if (query != null && !query.isEmpty()) {

            bookings = bookingRepository.findByHotelIdAndStatusAndRoomNameContainingOrUserFullNameContaining(
                hotelId, status, query, pageable);
        } else {

            bookings = bookingRepository.findByHotelIdAndStatus(hotelId, status, pageable);
        }
        

        return bookings.stream()
                .map(this::convertToBookingResponseDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Hỗ trợ phương thức tạo Pageable từ offset, limit và order
     */
    private Pageable createPageable(Integer offset, Integer limit, String order) {
        Sort sort;
        

        if ("asc".equalsIgnoreCase(order)) {
            sort = Sort.by(Sort.Direction.ASC, "createdAt");
        } else {
            sort = Sort.by(Sort.Direction.DESC, "createdAt");
        }
        
  
        int page = offset / limit;
        
        return PageRequest.of(page, limit, sort);
    }
    
  
    private BookingHotelOwnerDto convertToBookingResponseDto(Booking booking) {
        BookingHotelOwnerDto dto = new BookingHotelOwnerDto();
        
        dto.setBookingId(booking.getBookingId());
        dto.setCheckIn(booking.getCheckIn());
        dto.setCheckOut(booking.getCheckOut());
        dto.setPrice(booking.getPrice());
        dto.setStatus(booking.getStatus().name());
        dto.setCreatedAt(booking.getCreatedAt());
        
        // User information
        if (booking.getUser() != null) {
            UserDto userDto = new UserDto();
            userDto.setUserId(booking.getUser().getUserId());
            userDto.setFullName(booking.getUser().getFullName());
            userDto.setPhone(booking.getUser().getPhone());
            userDto.setEmail(booking.getUser().getEmail());
            dto.setUser(userDto);  // Make sure this setter exists
        }
        
        // Room name
        if (booking.getRoom() != null) {
            dto.setRoomName(booking.getRoom().getRoomName());  // Make sure this setter exists
        }
        
        // Bill information
        if (booking.getBill() != null) {
            dto.setBillId(booking.getBill().getBillId());  // Changed from getId() to getBillId()
        }
        
        // Reviews
        // Check if reviews are accessible through a different method
        if (booking.getReviewList() != null && !booking.getReviewList().isEmpty()) {  // Changed from getReviews() to getReviewList()
            List<Integer> reviewIds = booking.getReviewList().stream()
                    .map(Review::getReviewId)  // Changed from getId() to getReviewId()
                    .collect(Collectors.toList());
            dto.setReviewIdList(reviewIds);
        } else {
            dto.setReviewIdList(new ArrayList<>());
        }
        
        return dto;
    }

    public void setFirebaseMessagingService(FirebaseMessagingService firebaseMessagingService) {
        this.firebaseMessagingService = firebaseMessagingService;
    }

}