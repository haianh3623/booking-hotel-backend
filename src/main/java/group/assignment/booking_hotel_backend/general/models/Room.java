package group.assignment.booking_hotel_backend.general.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Integer roomId;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @Column(name = "room_name", nullable = false)
    private String roomName;

    @Column(name = "area", nullable = false)
    private Double area;

    @Column(name = "price_per_hour", nullable = false)
    private Double pricePerHour;

    @Column(name = "price_per_night", nullable = false)
    private Double pricePerNight;

    @Column(name = "extra_hour_price", nullable = false)
    private Double extraHourPrice;

    @Column(name = "standard_occupancy", nullable = false)
    private Integer standardOccupancy;

    @Column(name = "max_occupancy", nullable = false)
    private Integer maxOccupancy;

    @Column(name = "num_children_free", nullable = false)
    private Integer numChildrenFree;

    @Column(name = "available_room", nullable = false)
    private Integer availableRoom;

    @Column(name = "room_img")
    private String roomImg;

    @Column(name = "bed_number", nullable = false)
    private Integer bedNumber;

    @Column(name = "time_created")
    private LocalDateTime timeCreated;

    // Default constructor
    public Room() {
    }

    // Constructor with required fields
    public Room(Hotel hotel, String roomName, Double area, Double pricePerHour, Double pricePerNight,
                Double extraHourPrice, Integer standardOccupancy, Integer maxOccupancy,
                Integer numChildrenFree, Integer availableRoom, Integer bedNumber) {
        this.hotel = hotel;
        this.roomName = roomName;
        this.area = area;
        this.pricePerHour = pricePerHour;
        this.pricePerNight = pricePerNight;
        this.extraHourPrice = extraHourPrice;
        this.standardOccupancy = standardOccupancy;
        this.maxOccupancy = maxOccupancy;
        this.numChildrenFree = numChildrenFree;
        this.availableRoom = availableRoom;
        this.bedNumber = bedNumber;
        this.timeCreated = LocalDateTime.now();
    }

    // Getters and Setters
    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public Double getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(Double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public Double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(Double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public Double getExtraHourPrice() {
        return extraHourPrice;
    }

    public void setExtraHourPrice(Double extraHourPrice) {
        this.extraHourPrice = extraHourPrice;
    }

    public Integer getStandardOccupancy() {
        return standardOccupancy;
    }

    public void setStandardOccupancy(Integer standardOccupancy) {
        this.standardOccupancy = standardOccupancy;
    }

    public Integer getMaxOccupancy() {
        return maxOccupancy;
    }

    public void setMaxOccupancy(Integer maxOccupancy) {
        this.maxOccupancy = maxOccupancy;
    }

    public Integer getNumChildrenFree() {
        return numChildrenFree;
    }

    public void setNumChildrenFree(Integer numChildrenFree) {
        this.numChildrenFree = numChildrenFree;
    }

    public Integer getAvailableRoom() {
        return availableRoom;
    }

    public void setAvailableRoom(Integer availableRoom) {
        this.availableRoom = availableRoom;
    }

    public String getRoomImg() {
        return roomImg;
    }

    public void setRoomImg(String roomImg) {
        this.roomImg = roomImg;
    }

    public Integer getBedNumber() {
        return bedNumber;
    }

    public void setBedNumber(Integer bedNumber) {
        this.bedNumber = bedNumber;
    }

    public LocalDateTime getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(LocalDateTime timeCreated) {
        this.timeCreated = timeCreated;
    }

    @PrePersist
    protected void onCreate() {
        if (timeCreated == null) {
            timeCreated = LocalDateTime.now();
        }
    }
}