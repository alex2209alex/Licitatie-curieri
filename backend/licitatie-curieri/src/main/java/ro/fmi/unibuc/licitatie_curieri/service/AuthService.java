package ro.fmi.unibuc.licitatie_curieri.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.fmi.unibuc.licitatie_curieri.common.exception.BadRequestException;
import ro.fmi.unibuc.licitatie_curieri.domain.user.entity.User;
import ro.fmi.unibuc.licitatie_curieri.domain.user.repository.UserRepository;
import ro.fmi.unibuc.licitatie_curieri.dto.LoginRequestDto;
import ro.fmi.unibuc.licitatie_curieri.service.EmailService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final EmailService emailService;

    public String initiateLogin(String username, String password) {
        // Verifică existența utilizatorului
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new BadRequestException("Invalid username or password"));

        // Verifică parola (hash-ul este recomandat)
        if (!user.getPassword().equals(password)) { // Înlocuiește cu hashing
            throw new BadRequestException("Invalid username or password");
        }

        // Generează un cod pentru autentificarea în doi pași
        String verificationCode = generateVerificationCode();
        user.setVerificationCode(verificationCode);
        user.setVerificationDeadline(Instant.now().plus(5, ChronoUnit.MINUTES));
        userRepository.save(user);

        // Trimite codul de verificare către utilizator (email/SMS)
        emailService.sendVerificationCode(user.getEmail(), verificationCode);

        return "Verification code sent";
    }

    private String generateVerificationCode() {
        return String.valueOf(new Random().nextInt(900000) + 100000); // Cod de 6 cifre
    }

    public String verifyCode(String username, String code) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new BadRequestException("User not found"));

        if (!user.getVerificationCode().equals(code)) {
            throw new BadRequestException("Invalid verification code");
        }

        if (Instant.now().isAfter(user.getVerificationDeadline())) {
            throw new BadRequestException("Verification code expired");
        }

        // Resetează verificarea
        user.setVerificationCode(null);
        user.setVerificationDeadline(null);
        userRepository.save(user);

        // Generează token JWT
        return jwtUtil.generateToken(user);
    }



}