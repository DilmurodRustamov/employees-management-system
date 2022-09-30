package uz.tax.employeemanagement2.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.tax.employeemanagement2.ref.UserRole;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends AbstractEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "user_role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private String position;

    private double salary;

    public User(String name, String phoneNumber, String password, UserRole userRole) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.userRole = userRole;
    }

    public static User initializeUserWithUserRole(String phoneNumber) {
        User user = new User();
        user.setPhoneNumber(phoneNumber);
        user.setUserRole(UserRole.USER);
        return user;
    }
}

