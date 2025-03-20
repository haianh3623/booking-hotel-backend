package group.assignment.booking_hotel_backend.general.dto;

import group.assignment.booking_hotel_backend.general.models.Users;
import java.time.LocalDateTime;

public class UsersDto {
    private Integer userId;
    private String fullname;
    private String phone;
    private String email;
    private String username;
    private String password;
    private Integer score;
    private LocalDateTime timeCreated;

    // No-args constructor
    public UsersDto() {
    }

    // All-args constructor
    public UsersDto(Integer userId, String fullname, String phone, String email, String username,
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

    // Constructor from Users entity
    public UsersDto(Users user) {
        this.userId = user.getUserId();
        this.fullname = user.getFullname();
        this.phone = user.getPhone();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.score = user.getScore();
        this.timeCreated = user.getTimeCreated();
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
        UsersDto usersDto = (UsersDto) o;
        return userId != null && userId.equals(usersDto.userId);
    }

    @Override
    public int hashCode() {
        return userId != null ? userId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "UsersDto{" +
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