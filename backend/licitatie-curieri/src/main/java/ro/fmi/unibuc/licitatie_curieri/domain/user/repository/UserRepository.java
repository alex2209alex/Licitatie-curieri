package ro.fmi.unibuc.licitatie_curieri.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.fmi.unibuc.licitatie_curieri.domain.user.entity.User;
import ro.fmi.unibuc.licitatie_curieri.domain.user.entity.UserType;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndUserType(String email, UserType userType);

    Optional<User> findByEmailAndEmailVerificationCodeAndPhoneVerificationCode(String email, String emailVerificationCode, String phoneVerificationCode);

    Optional<User> findByEmailAndPassword(String email, String password);
}
