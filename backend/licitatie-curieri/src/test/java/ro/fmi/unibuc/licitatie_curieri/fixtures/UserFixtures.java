package ro.fmi.unibuc.licitatie_curieri.fixtures;

import lombok.val;
import org.openapitools.model.UserCreationDto;
import org.openapitools.model.UserCreationResponseDto;
import org.openapitools.model.UserTypeDto;

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
}
