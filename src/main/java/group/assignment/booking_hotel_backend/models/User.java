package group.assignment.booking_hotel_backend.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    @Column(name = "avatar_url")
    private String avatarUrl;
    private String fullName;
    private String phone;
    private String email;
    @Column(unique = true)
    private String username;
    private String password;
    private Integer score = 0;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="user_role",
            joinColumns=@JoinColumn(name="user_id"),
            inverseJoinColumns=@JoinColumn(name="role_id")
    )
    private List<Role> roleList;

    @OneToMany(mappedBy="user")
    private List<Hotel> hotelList;

    @OneToMany(mappedBy="user")
    private List<Booking> bookingList;

    @OneToMany(mappedBy="user")
    private List<Bill> billList;

    @OneToMany(mappedBy="user")
    private List<Notification> notiList;


    public void addRole(Role role) {
        if (roleList == null) {
            roleList = new ArrayList<>();
        }
        roleList.add(role);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", fullName='" + fullName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", score=" + score +
                '}';
    }
}

