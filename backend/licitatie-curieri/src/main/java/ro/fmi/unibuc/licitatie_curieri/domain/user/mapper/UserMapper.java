package ro.fmi.unibuc.licitatie_curieri.domain.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.openapitools.model.*;
import ro.fmi.unibuc.licitatie_curieri.common.exception.InternalServerErrorException;
import ro.fmi.unibuc.licitatie_curieri.common.utils.ErrorMessageUtils;
import ro.fmi.unibuc.licitatie_curieri.domain.user.entity.User;
import ro.fmi.unibuc.licitatie_curieri.domain.user.entity.UserType;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface UserMapper {
    UserType mapToUserType(UserTypeDto userType);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", source = "password", qualifiedByName = "hashPassword")
    @Mapping(target = "emailVerificationCode", expression = "java(this.getVerificationCode())")
    @Mapping(target = "phoneVerificationCode", expression = "java(this.getVerificationCode())")
    @Mapping(target = "verificationDeadline", expression = "java(this.getVerificationDeadline())")
    @Mapping(target = "verified", constant = "false")
    @Mapping(target = "userAddressAssociations", ignore = true)
    User mapToUser(UserCreationDto userCreationDto);

    UserCreationResponseDto mapToUserCreationResponseDto(User user);

    UserLoginResponseDto mapToUserLoginResponseDto(String  token);

    @Named("hashPassword")
    default String hashPassword(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            return new String(messageDigest.digest(password.getBytes(StandardCharsets.UTF_16)), StandardCharsets.UTF_16);
        } catch (NoSuchAlgorithmException exception) {
            throw new InternalServerErrorException(ErrorMessageUtils.ERROR_HASHING_ALGORITHM);
        }
    }

    default String getVerificationCode() {
        SecureRandom secureRandom = new SecureRandom();
        return Integer.toString(secureRandom.nextInt(90000000) + 10000000);
    }

    default Instant getVerificationDeadline() {
        return Instant.now().plusSeconds(300);
    }
}
