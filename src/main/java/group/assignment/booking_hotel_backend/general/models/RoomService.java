package group.assignment.booking_hotel_backend.general.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.io.Serializable;

@Entity
@Table(name = "RoomService")
public class RoomService {

    @EmbeddedId
    private RoomServiceId id;

    @ManyToOne
    @MapsId("serviceId")
    @JoinColumn(name = "service_id")
    private Service service;

    @ManyToOne
    @MapsId("roomId")
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(name = "time_created")
    private LocalDateTime timeCreated;

    // Default constructor
    public RoomService() {
    }

    // Constructor with required fields
    public RoomService(Service service, Room room) {
        this.id = new RoomServiceId(service.getServiceId(), room.getRoomId());
        this.service = service;
        this.room = room;
        this.timeCreated = LocalDateTime.now();
    }

    // Getters and Setters
    public RoomServiceId getId() {
        return id;
    }

    public void setId(RoomServiceId id) {
        this.id = id;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
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

    // Composite key class
    @Embeddable
    public static class RoomServiceId implements Serializable {

        @Column(name = "service_id")
        private Integer serviceId;

        @Column(name = "room_id")
        private Integer roomId;

        // Default constructor
        public RoomServiceId() {
        }

        public RoomServiceId(Integer serviceId, Integer roomId) {
            this.serviceId = serviceId;
            this.roomId = roomId;
        }

        // Getters and Setters
        public Integer getServiceId() {
            return serviceId;
        }

        public void setServiceId(Integer serviceId) {
            this.serviceId = serviceId;
        }

        public Integer getRoomId() {
            return roomId;
        }

        public void setRoomId(Integer roomId) {
            this.roomId = roomId;
        }

        // equals and hashCode
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RoomServiceId that = (RoomServiceId) o;

            if (!serviceId.equals(that.serviceId)) return false;
            return roomId.equals(that.roomId);
        }

        @Override
        public int hashCode() {
            int result = serviceId.hashCode();
            result = 31 * result + roomId.hashCode();
            return result;
        }
    }
}