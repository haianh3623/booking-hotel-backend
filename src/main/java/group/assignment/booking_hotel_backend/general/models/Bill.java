package group.assignment.booking_hotel_backend.general.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Bill")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bill_id")
    private Integer billId;

    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    @Column(name = "paid_status", nullable = false)
    private Boolean paidStatus;

    @OneToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @Column(name = "time_created")
    private LocalDateTime timeCreated;

    // Default constructor
    public Bill() {
    }

    // Constructor with required fields
    public Bill(Double totalPrice, Boolean paidStatus, Booking booking) {
        this.totalPrice = totalPrice;
        this.paidStatus = paidStatus;
        this.booking = booking;
        this.timeCreated = LocalDateTime.now();
    }

    // Getters and Setters
    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Boolean getPaidStatus() {
        return paidStatus;
    }

    public void setPaidStatus(Boolean paidStatus) {
        this.paidStatus = paidStatus;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
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