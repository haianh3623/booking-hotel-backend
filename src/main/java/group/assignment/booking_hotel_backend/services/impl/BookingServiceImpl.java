package group.assignment.booking_hotel_backend.services.impl;

import group.assignment.booking_hotel_backend.dto.*;
import group.assignment.booking_hotel_backend.mapper.BookingMapper;
import group.assignment.booking_hotel_backend.models.*;
import group.assignment.booking_hotel_backend.repository.*;
import group.assignment.booking_hotel_backend.services.BookingService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final BillRepository billRepository;
    private final UserRepository userRepository;
//    @Override
//    public List<BookingSearchResponse> searchAvailableRooms(BookingSearchRequest request) {
//        System.out.println(request);
//        List<Hotel> hotels;
//
//        if (request.getCity() != null && !request.getCity().isEmpty()
//                && request.getDistrict() != null && !request.getDistrict().isEmpty()) {
//            hotels = hotelRepository.findByAddressCityAndAddressDistrict(
//                    request.getCity(), request.getDistrict()
//            );
//        } else {
//            hotels = hotelRepository.findAll();
//        }
//
//        System.out.println("Hi1");
//        for (Hotel hotel : hotels) {
//            System.out.println(hotel.getHotelName());
//        }
//
//        List<BookingSearchResponse> results = new ArrayList<>();
//
//        LocalDateTime checkIn = parseDateTime(request.getCheckInDate(), request.getCheckInTime());
//        LocalDateTime checkOut = parseDateTime(request.getCheckOutDate(), request.getCheckOutTime());
//
//        for (Hotel hotel : hotels) {
//            for (Room room : hotel.getRoomList()) {
//                // 2. Lọc dịch vụ theo yêu cầu
//                List<String> roomServiceNames = room.getServiceList().stream()
//                        .map(Service::getServiceName)
//                        .collect(Collectors.toList());
//
//                String keyword = request.getInfoSearch();
//                if (keyword != null && !keyword.trim().isEmpty()) {
//                    String lowerKeyword = keyword.trim().toLowerCase();
//
//                    String hotelName = hotel.getHotelName() != null ? hotel.getHotelName().toLowerCase() : "";
//                    String roomName = room.getRoomName() != null ? room.getRoomName().toLowerCase() : "";
//                    String roomDesc = room.getDescription() != null ? room.getDescription().toLowerCase() : "";
//                    String city = hotel.getAddress().getCity() != null ? hotel.getAddress().getCity().toLowerCase() : "";
//                    String district = hotel.getAddress().getDistrict() != null ? hotel.getAddress().getDistrict().toLowerCase() : "";
//                    String ward = hotel.getAddress().getWard() != null ? hotel.getAddress().getWard().toLowerCase() : "";
//                    String specific = hotel.getAddress().getSpecificAddress() != null ? hotel.getAddress().getSpecificAddress().toLowerCase() : "";
//
//                    boolean match = hotelName.contains(lowerKeyword)
//                            || roomName.contains(lowerKeyword)
//                            || roomDesc.contains(lowerKeyword)
//                            || city.contains(lowerKeyword)
//                            || district.contains(lowerKeyword)
//                            || ward.contains(lowerKeyword)
//                            || specific.contains(lowerKeyword);
//
//                    if (!match) continue;
//                }
//
//                if (request.getServices() != null && !roomServiceNames.containsAll(request.getServices())) {
//                    continue;
//                }
//                if(request.getAdults() + request.getChildren() > room.getMaxOccupancy()){
//                    continue;
//                }
//                if(request.getBedNumber() != room.getBedNumber()){
//                    continue;
//                }
//                System.out.println("Hi2");
//                for (String serviceName : request.getServices()) {
//                    System.out.println(serviceName);
//                }
//
//
//                // 3. Kiểm tra xem phòng có bị trùng lịch không
//                boolean isAvailable = isAvailable(room, checkIn, checkOut);
//                if (!isAvailable) continue;
//
//                System.out.println("Hi3");
//                System.out.println(isAvailable);
//
//
//                // 4. Tính tổng giá tiền
//                double price = calculateTotalPrice(request, room);
//                System.out.println(4);
//                System.out.println(price);
//
//                // 5. Lọc theo khoảng giá
//                if (request.getPriceFrom() != null && price < request.getPriceFrom()) continue;
//                if (request.getPriceTo() != null && price > request.getPriceTo()) continue;
//                System.out.println(5);
//
//                results.add(BookingSearchResponse.builder()
//                        .roomId(room.getRoomId())
//                        .roomName(room.getRoomName())
//                        .price(price)
//                        .hotelName(hotel.getHotelName())
//                        .roomImg(room.getRoomImg())
//                        .address(hotel.getAddress().getSpecificAddress())
//                        .services(roomServiceNames)
//                        .checkIn(formatDateTime(checkIn))
//                        .checkOut(formatDateTime(checkOut))
//                        .adults(request.getAdults())
//                        .children(request.getChildren())
//                        .bedNumber(request.getBedNumber())
//                        .build());
//            }
//        }
//
//        System.out.println("Hi4");
//        System.out.println(results.size());
//        for (BookingSearchResponse result : results) {
//            System.out.println(result);
//        }
//
//        // 6. Sắp xếp
//        Comparator<BookingSearchResponse> comparator = null;
//
//        switch (request.getSortBy()) {
//            case "price_asc":
//                comparator = Comparator.comparing(BookingSearchResponse::getPrice);
//                break;
//            case "price_desc":
//                comparator = Comparator.comparing(BookingSearchResponse::getPrice).reversed();
//                break;
//            case "rating_asc":
//                comparator = Comparator.comparing(BookingSearchResponse::getRating);
//                break;
//            case "rating_desc":
//                comparator = Comparator.comparing(BookingSearchResponse::getRating).reversed();
//                break;
//            default:
//                // Nếu không có sortBy hoặc không khớp, sắp xếp theo giá tăng dần mặc định
//                comparator = Comparator.comparing(BookingSearchResponse::getPrice);
//                break;
//        }
//
//        results.sort(comparator);
//        return results;
//    }

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

                // ✅ Tính rating trung bình của phòng
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
        bookingRepository.deleteById(id);
    }

    @Override
    public Booking updateBookingStatus(Integer bookingId, BookingStatus newStatus) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setStatus(newStatus);
        Booking updatedBooking = bookingRepository.save(booking);
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

        LocalDateTime standardCheckInTime = LocalDateTime.of(checkInDateTimeParsed.toLocalDate(), LocalTime.of(14, 0));
        LocalDateTime standardCheckOutTime = LocalDateTime.of(checkOutDateTimeParsed.toLocalDate(), LocalTime.of(11, 0));

        if (checkInDateTimeParsed.isBefore(standardCheckInTime)) {
            standardCheckInTime = standardCheckInTime.minusDays(1);
        }

        LocalDateTime nextCheckInTime = standardCheckInTime.plusDays(1);
        long earlyCheckInHours = ChronoUnit.HOURS.between(checkInDateTimeParsed, nextCheckInTime);
        if (earlyCheckInHours < 0) earlyCheckInHours = 0;

        long lateCheckOutHours = ChronoUnit.HOURS.between(standardCheckOutTime, checkOutDateTimeParsed);
        if (lateCheckOutHours < 0) lateCheckOutHours = 0;

        if (earlyCheckInHours >= 24) {
            earlyCheckInHours -= 24;
        }

        long totalStayDurationInDays = ChronoUnit.DAYS.between(checkInDateTimeParsed.minusDays(1), standardCheckOutTime);
        Double totalStayPrice = (Double) (totalStayDurationInDays * pricePerNight + (earlyCheckInHours + lateCheckOutHours) * extraHourRate + extraCharges);

        long totalDurationInHours = ChronoUnit.HOURS.between(checkInDateTimeParsed, checkOutDateTimeParsed);
        Double timeBasedPrice = 0D;

        if (totalDurationInHours >= 2) {
            timeBasedPrice = comboPriceFor2Hours + extraHourRate * (totalDurationInHours - 2);
        } else if (totalDurationInHours == 1) {
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
        }

        return AdminRevenueResponse.builder()
                .bookingDetailList(bookingDetails)
                .totalRevenue(totalRevenue)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    @Override
    public List<Booking> findAllBookingsByHotelOwner(Integer userId) {
        return bookingRepository.findAllBookingsByHotelOwner(userId);
    }

}
