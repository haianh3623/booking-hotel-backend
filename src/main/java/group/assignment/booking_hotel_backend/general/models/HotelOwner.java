package group.assignment.booking_hotel_backend.general.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "HotelOwner")
public class HotelOwner {

    @Id
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "time_created")
    private LocalDateTime timeCreated;

    @OneToOne
    @JoinColumn(name = "user_id")
    @MapsId
    private Users user;

    // Default constructor
    public HotelOwner() {
    }

    // Constructor with user
    public HotelOwner(Users user) {
        this.user = user;
        this.timeCreated = LocalDateTime.now();
    }

    // Getters and Setters
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public LocalDateTime getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(LocalDateTime timeCreated) {
        this.timeCreated = timeCreated;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    @PrePersist
    protected void onCreate() {
        if (timeCreated == null) {
            timeCreated = LocalDateTime.now();
        }
    }
}