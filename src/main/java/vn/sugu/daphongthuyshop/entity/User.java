package vn.sugu.daphongthuyshop.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import vn.sugu.daphongthuyshop.enums.Role;

@Entity
@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString(exclude = { "cart" })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String userId;
    String fullName;
    String email;
    String password;
    String dob;
    String gender;
    String phone;
    String address;

    @Enumerated(EnumType.STRING)
    Role role; // CUSTOMER, ADMIN

    String avatarUrl;

    @Column(columnDefinition = "MEDIUMTEXT")
    String accessToken;
    @Column(columnDefinition = "MEDIUMTEXT")
    String refreshToken;
    @Column(columnDefinition = "MEDIUMTEXT")
    String resetPasswordToken;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<Order> orders;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<Review> reviews;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    Cart cart;
}
