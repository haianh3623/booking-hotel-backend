package group.assignment.booking_hotel_backend.services.impl;

import group.assignment.booking_hotel_backend.dto.BookingSearchRequest;
import group.assignment.booking_hotel_backend.dto.BookingSearchResponse;
import group.assignment.booking_hotel_backend.models.Booking;
import group.assignment.booking_hotel_backend.models.Hotel;
import group.assignment.booking_hotel_backend.models.Room;
import group.assignment.booking_hotel_backend.models.Service;
import group.assignment.booking_hotel_backend.repository.BookingRepository;
import group.assignment.booking_hotel_backend.repository.HotelRepository;
import group.assignment.booking_hotel_backend.repository.RoomRepository;
import group.assignment.booking_hotel_backend.services.BookingService;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    @Override
    public List<BookingSearchResponse> searchAvailableRooms(BookingSearchRequest request) {
        System.out.println(request);
        List<Hotel> hotels;

        if (request.getCity() != null && !request.getCity().isEmpty()
                && request.getDistrict() != null && !request.getDistrict().isEmpty()) {
            hotels = hotelRepository.findByAddressCityAndAddressDistrict(
                    request.getCity(), request.getDistrict()
            );
        } else {
            hotels = hotelRepository.findAll();
        }

        System.out.println("Hi1");
        for (Hotel hotel : hotels) {
            System.out.println(hotel.getHotelName());
        }

        List<BookingSearchResponse> results = new ArrayList<>();

        LocalDateTime checkIn = parseDateTime(request.getCheckInDate(), request.getCheckInTime());
        LocalDateTime checkOut = parseDateTime(request.getCheckOutDate(), request.getCheckOutTime());

        for (Hotel hotel : hotels) {
            for (Room room : hotel.getRoomList()) {
                // 2. Lọc dịch vụ theo yêu cầu
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
                if(request.getAdults() + request.getChildren() > room.getMaxOccupancy()){
                    continue;
                }
                if(request.getBedNumber() != room.getBedNumber()){
                    continue;
                }
                System.out.println("Hi2");
                for (String serviceName : request.getServices()) {
                    System.out.println(serviceName);
                }


                // 3. Kiểm tra xem phòng có bị trùng lịch không
                boolean isAvailable = isAvailable(room, checkIn, checkOut);
                if (!isAvailable) continue;

                System.out.println("Hi3");
                System.out.println(isAvailable);


                // 4. Tính tổng giá tiền
                double price = calculateTotalPrice(request, room);
                System.out.println(4);
                System.out.println(price);

                // 5. Lọc theo khoảng giá
                if (request.getPriceFrom() != null && price < request.getPriceFrom()) continue;
                if (request.getPriceTo() != null && price > request.getPriceTo()) continue;
                System.out.println(5);

                results.add(BookingSearchResponse.builder()
                        .roomId(room.getRoomId())
                        .roomName(room.getRoomName())
                        .price(price)
                        .hotelName(hotel.getHotelName())
                        .address(hotel.getAddress().getSpecificAddress())
                        .services(roomServiceNames)
                        .checkIn(formatDateTime(checkIn))
                        .checkOut(formatDateTime(checkOut))
                        .build());
            }
        }

        System.out.println("Hi4");
        System.out.println(results.size());
        for (BookingSearchResponse result : results) {
            System.out.println(result);
        }

        // 6. Sắp xếp
        Comparator<BookingSearchResponse> comparator = Comparator.comparing(BookingSearchResponse::getPrice);
        if ("price_desc".equals(request.getSortBy())) {
            comparator = comparator.reversed();
        }

        results.sort(comparator);
        return results;
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
}
