package group.assignment.booking_hotel_backend.general.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "role_name", length = 50, nullable = false, unique = true)
    private String roleName;

    @Column(name = "time_created")
    private LocalDateTime timeCreated;

    // Default constructor
    public Role() {
    }

    // Constructor with role name
    public Role(String roleName) {
        this.roleName = roleName;
        this.timeCreated = LocalDateTime.now();
    }

    // Getters and Setters
    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
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