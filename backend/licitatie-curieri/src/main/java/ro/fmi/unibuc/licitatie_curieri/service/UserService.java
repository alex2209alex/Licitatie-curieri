package ro.fmi.unibuc.licitatie_curieri.service;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.fmi.unibuc.licitatie_curieri.common.EmailSender;
import ro.fmi.unibuc.licitatie_curieri.common.SmsSender;
import ro.fmi.unibuc.licitatie_curieri.common.exception.*;
import ro.fmi.unibuc.licitatie_curieri.common.security.JwtUtils;
import ro.fmi.unibuc.licitatie_curieri.common.utils.ErrorMessageUtils;
import ro.fmi.unibuc.licitatie_curieri.controller.user.models.*;
import ro.fmi.unibuc.licitatie_curieri.domain.user.entity.User;
import ro.fmi.unibuc.licitatie_curieri.domain.user.mapper.UserMapper;
import ro.fmi.unibuc.licitatie_curieri.domain.user.repository.UserRepository;

import javax.mail.MessagingException;
import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserCreationResponseDto createUser(UserCreationDto userCreationDto) {
        userRepository.findByEmailAndUserType(userCreationDto.getEmail(), userMapper.mapToUserType(userCreationDto.getUserType()))
                .ifPresent(this::ensureUserIsUnverifiedAndVerificationTimeExpired);

        ensureValidEmail(userCreationDto.getEmail());
        ensureValidPhoneNumber(userCreationDto.getPhoneNumber());
        ensureValidPassword(userCreationDto.getPassword(), userCreationDto.getPasswordConfirmation());

        val user = userMapper.mapToUser(userCreationDto);

        try {
            EmailSender.sendEmail(
                    user.getEmail(),
                    user.getEmailVerificationCode(),
                    "Verification code from Licitatie-Curieri",
                    "Your code for verification new user account is: " + user.getEmailVerificationCode()
            );
        } catch (MessagingException e) {
            throw new InternalServerErrorException(e.getMessage());
        }

        SmsSender smsSender = new SmsSender();
        smsSender.sendSms(user.getPhoneNumber(), user.getPhoneVerificationCode());

        return userMapper.mapToUserCreationResponseDto(userRepository.save(user));
    }

    @Transactional(noRollbackFor = ForbiddenException.class)
    public void verifyUser(String email, UserVerificationDto userVerificationDto) {
        val user = userRepository.findByEmailAndEmailVerificationCodeAndPhoneVerificationCode(email, userVerificationDto.getEmailVerificationCode(), userVerificationDto.getPhoneVerificationCode())
                .orElseThrow(() -> new BadRequestException(ErrorMessageUtils.USER_VERIFICATION_FAILED));

        if (Instant.now().isAfter(user.getVerificationDeadline())) {
            userRepository.delete(user);
            throw new ForbiddenException(ErrorMessageUtils.VERIFICATION_FAILED_USER_DELETED);
        }

        user.setVerified(true);
        user.setEmailVerificationCode(null);
        user.setPhoneVerificationCode(null);
        user.setVerificationDeadline(null);
    }

    @Transactional
    public UserLoginResponseDto loginUser(UserLoginDto userLoginDto) {
        val user = userRepository.findByEmailAndPassword(userLoginDto.getEmail(), userMapper.hashPassword(userLoginDto.getPassword()))
                .orElseThrow(() -> new UnauthorizedException(String.format(ErrorMessageUtils.AUTHORIZATION_FAILED)));

        try {
            String code = EmailSender.generateCode();
            EmailSender.sendEmail(
                    user.getEmail(),
                    code,
                    "2FA Code from Licitatie-Curieri",
                    "Your code for 2FA login is: " + code
            );
            user.setTwoFACode(code);
            user.setVerifyFaCodeDeadline(Instant.now().plusSeconds(300));
            userRepository.save(user);
        } catch (MessagingException e) {
            throw new InternalServerErrorException(e.getMessage());
        }

        return userMapper.mapToUserLoginResponseDto(JwtUtils.generateToken(user.getId(), user.getUserType()));
    }

    @Transactional(noRollbackFor = ForbiddenException.class)
    public void getTwoFACodeUser(UserTwoFAVerificationDto userTwoFAVerificationDto) {
        val user = userRepository.findByEmailAndTwoFACode(
                        userTwoFAVerificationDto.getEmail(),
                        userTwoFAVerificationDto.getVerificationCode()
                )
                .orElseThrow(() -> new NotFoundException(String.format(ErrorMessageUtils.USER_NOT_FOUND, userTwoFAVerificationDto.getEmail())));

        if (Instant.now().isAfter(user.getVerifyFaCodeDeadline())) {
            user.setTwoFACode(null);
            user.setVerifyFaCodeDeadline(null);
            throw new ForbiddenException(ErrorMessageUtils.VERIFICATION_FAILED_TWO_FA);
        } else {
            user.setTwoFACode(null);
            user.setVerifyFaCodeDeadline(null);
        }
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

    private void ensureValidEmail(String email) {
        // Source regex https://stackoverflow.com/questions/8204680/java-regex-email
        Pattern pattern = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            throw new BadRequestException(ErrorMessageUtils.EMAIL_INVALID);
        }
    }

    private void ensureValidPhoneNumber(String phoneNumber) {
        Pattern pattern = Pattern.compile("^[+][\\d]{11}$");
        Matcher matcher = pattern.matcher(phoneNumber);
        if (!matcher.find()) {
            throw new BadRequestException(ErrorMessageUtils.PHONE_NUMBER_INVALID);
        }
    }

    private void ensureValidPassword(String password, String passwordConfirmation) {
        if (!password.equals(passwordConfirmation)) {
            throw new BadRequestException(ErrorMessageUtils.PASSWORD_IS_DIFFERENT_FROM_PASSWORD_CONFIRMATION);
        }

        ensureValidPasswordPattern(password);
    }

    private void ensureValidPasswordPattern(String password) {
        // Source regex https://stackoverflow.com/questions/3802192/regexp-java-for-password-validation
        Pattern pattern = Pattern.compile("^(?=.*[\\d])(?=.*[a-z])(?=.*[A-Z])(?=.*[~!@#$%^&*()_+])(?=\\S+$)");
        Matcher matcher = pattern.matcher(password);
        if (!matcher.find()) {
            throw new BadRequestException(ErrorMessageUtils.PASSWORD_INVALID);
        }
    }
}
