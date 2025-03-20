package group.assignment.booking_hotel_backend.general.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "User")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(nullable = false)
    private String fullname;

    @Column(nullable = false, unique = true, length = 15)
    private String phone;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(columnDefinition = "int default 0")
    private Integer score = 0;

    @Column(name = "time_created")
    private LocalDateTime timeCreated;

    // No-args constructor
    public Users() {
    }

    // All-args constructor
    public Users(Integer userId, String fullname, String phone, String email, String username,
                 String password, Integer score, LocalDateTime timeCreated) {
        this.userId = userId;
        this.fullname = fullname;
        this.phone = phone;
        this.email = email;
        this.username = username;
        this.password = password;
        this.score = score;
        this.timeCreated = timeCreated;
    }

    @PrePersist
    protected void onCreate() {
        timeCreated = LocalDateTime.now();
    }

    // Getters
    public Integer getUserId() {
        return userId;
    }

    public String getFullname() {
        return fullname;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Integer getScore() {
        return score;
    }

    public LocalDateTime getTimeCreated() {
        return timeCreated;
    }

    // Setters
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public void setTimeCreated(LocalDateTime timeCreated) {
        this.timeCreated = timeCreated;
    }

    // equals, hashCode, and toString methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Users users = (Users) o;
        return userId != null && userId.equals(users.userId);
    }

    @Override
    public int hashCode() {
        return userId != null ? userId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Users{" +
                "userId=" + userId +
                ", fullname='" + fullname + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='[PROTECTED]'" +
                ", score=" + score +
                ", timeCreated=" + timeCreated +
                '}';
    }
}