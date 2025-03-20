package group.assignment.booking_hotel_backend.general.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.io.Serializable;

@Entity
@Table(name = "UserRole")
public class UserRole {

    @EmbeddedId
    private UserRoleId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @MapsId("roleId")
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(name = "time_created")
    private LocalDateTime timeCreated;

    // Default constructor
    public UserRole() {
    }

    // Constructor with user and role
    public UserRole(Users user, Role role) {
        this.user = user;
        this.role = role;
        this.id = new UserRoleId(user.getUserId(), role.getRoleId());
        this.timeCreated = LocalDateTime.now();
    }

    // Getters and Setters
    public UserRoleId getId() {
        return id;
    }

    public void setId(UserRoleId id) {
        this.id = id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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

    // Composite primary key class
    @Embeddable
    public static class UserRoleId implements Serializable {

        @Column(name = "user_id")
        private Integer userId;

        @Column(name = "role_id")
        private Integer roleId;

        // Default constructor
        public UserRoleId() {
        }

        public UserRoleId(Integer userId, Integer roleId) {
            this.userId = userId;
            this.roleId = roleId;
        }

        // Getters and Setters
        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public Integer getRoleId() {
            return roleId;
        }

        public void setRoleId(Integer roleId) {
            this.roleId = roleId;
        }

        // equals and hashCode methods
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            UserRoleId that = (UserRoleId) o;

            if (!userId.equals(that.userId)) return false;
            return roleId.equals(that.roleId);
        }

        @Override
        public int hashCode() {
            int result = userId.hashCode();
            result = 31 * result + roleId.hashCode();
            return result;
        }
    }
}