package ro.fmi.unibuc.licitatie_curieri.fixtures;

import lombok.val;
import org.openapitools.model.UserCreationDto;
import org.openapitools.model.UserCreationResponseDto;
import org.openapitools.model.UserTypeDto;
import ro.fmi.unibuc.licitatie_curieri.domain.user.entity.User;
import ro.fmi.unibuc.licitatie_curieri.domain.user.entity.UserType;

import java.time.Instant;

public class UserFixtures {
    private UserFixtures(){
    }

    public static UserCreationDto getUserCreationDtoFixture(){
        return new UserCreationDto("firstName", "lastName", "email", "phoneNumber", "password", "password", UserTypeDto.CLIENT);
    }

    public static UserCreationResponseDto getUserCreationResponseDtoFixture(){
        val userCreationResponseDto = new UserCreationResponseDto();
        userCreationResponseDto.setId(1L);
        userCreationResponseDto.setFirstName("firstName");
        userCreationResponseDto.setLastName("lastName");
        userCreationResponseDto.setEmail("email");
        userCreationResponseDto.setPhoneNumber("phoneNumber");
        userCreationResponseDto.setUserType(UserTypeDto.CLIENT);
        return userCreationResponseDto;
    }

    public static User getUnverifiedUserFixture() {
        val user = new User();
        user.setId(1L);
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail("email");
        user.setPhoneNumber("phoneNumber");
        user.setPassword("password");
        user.setUserType(UserType.CLIENT);
        user.setEmailVerificationCode("12345");
        user.setPhoneVerificationCode("54321");
        user.setVerificationDeadline(Instant.now().plusSeconds(300));
        user.setVerified(false);
        return user;
    }
}
