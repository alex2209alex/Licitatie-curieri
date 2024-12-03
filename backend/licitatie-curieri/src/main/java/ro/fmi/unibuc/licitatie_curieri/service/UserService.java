package ro.fmi.unibuc.licitatie_curieri.service;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.openapitools.model.UserCreationDto;
import org.openapitools.model.UserCreationResponseDto;
import org.openapitools.model.UserVerificationDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.fmi.unibuc.licitatie_curieri.common.exception.BadRequestException;
import ro.fmi.unibuc.licitatie_curieri.common.utils.ErrorMessageUtils;
import ro.fmi.unibuc.licitatie_curieri.domain.user.entity.User;
import ro.fmi.unibuc.licitatie_curieri.domain.user.mapper.UserMapper;
import ro.fmi.unibuc.licitatie_curieri.domain.user.repository.UserRepository;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserCreationResponseDto createUser(UserCreationDto userCreationDto) {
        userRepository.findByEmailAndUserType(userCreationDto.getEmail(), userMapper.mapToUserType(userCreationDto.getUserType()))
                .ifPresent(this::ensureUserIsUnverifiedAndVerificationTimeExpired);

//        ensureValidEmail(userCreationDto.getEmail());
//        ensureValidPhoneNumber(userCreationDto.getPhoneNumber());
//        ensureValidPassword(userCreationDto.getPassword(), userCreationDto.getPasswordConfirmation());

        val user = userMapper.mapToUser(userCreationDto);

        return userMapper.mapToUserCreationResponseDto(userRepository.save(user));
    }

    @Transactional
    public void verifyUser(String email, UserVerificationDto userVerificationDto) {
        val user = userRepository.findByEmailAndEmailVerificationCodeAndPhoneVerificationCode(email, userVerificationDto.getEmailVerificationCode(), userVerificationDto.getPhoneVerificationCode())
                .orElseThrow(() -> new BadRequestException(ErrorMessageUtils.USER_VERIFICATION_FAILED));

        if (Instant.now().isAfter(user.getVerificationDeadline())) {
            userRepository.delete(user);
            throw new BadRequestException(ErrorMessageUtils.VERIFICATION_FAILED_USER_DELETED);
        }

        user.setVerified(true);
        user.setEmailVerificationCode(null);
        user.setPhoneVerificationCode(null);
        user.setVerificationDeadline(null);
    }

    private void ensureUserIsUnverifiedAndVerificationTimeExpired(User user) {
        if (user.isVerified()) {
            throw new BadRequestException(String.format(ErrorMessageUtils.EMAIL_ALREADY_USED_FOR_USER_TYPE, user.getEmail(), user.getUserType()));
        }
        if (Instant.now().isBefore(user.getVerificationDeadline())) {
            throw new BadRequestException(String.format(ErrorMessageUtils.USER_WITH_EMAIL_AND_USER_TYPE_AWAITING_VERIFICATION, user.getEmail(), user.getUserType()));
        }
        userRepository.delete(user);
    }

    private void ensureValidPassword(String password, String passwordConfirmation) {
        if (!password.equals(passwordConfirmation)) {
            throw new BadRequestException(ErrorMessageUtils.PASSWORD_IS_DIFFERENT_FROM_PASSWORD_CONFIRMATION);
        }

        // Sursa regex https://www.baeldung.com/java-regex-password-validation
        String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\\\d)(?=.*[~!@#$%^&*()_+])";

        if (!password.matches(pattern)) {
            throw new BadRequestException(ErrorMessageUtils.PASSWORD_INVALID);
        }
    }
}
