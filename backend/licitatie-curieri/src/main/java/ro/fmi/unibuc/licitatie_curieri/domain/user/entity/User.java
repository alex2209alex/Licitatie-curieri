package ro.fmi.unibuc.licitatie_curieri.domain.user.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"id", "user_type"})})
@Data
@NoArgsConstructor
public class User {
    @Id
    @SequenceGenerator(name = "users_gen", sequenceName = "users_seq", allocationSize = 20)
    @GeneratedValue(generator = "users_gen")
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    private UserType userType;

    @Column(name = "email_verification_code")
    private String emailVerificationCode;

    @Column(name = "phone_verification_code")
    private String phoneVerificationCode;

    @Column(name = "verification_deadline")
    private Instant verificationDeadline;

    @Column(name = "is_verified")
    private boolean isVerified;

    @Column(name = "two_fa_code")
    private String twoFACode;

    @Column(name = "verify_fa_code_deadline")
    private Instant verifyFaCodeDeadline;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<UserAddressAssociation> userAddressAssociations = new ArrayList<>();
}
