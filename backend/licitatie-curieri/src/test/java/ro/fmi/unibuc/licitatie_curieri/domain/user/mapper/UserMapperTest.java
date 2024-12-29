package ro.fmi.unibuc.licitatie_curieri.domain.user.mapper;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openapitools.model.UserTypeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ro.fmi.unibuc.licitatie_curieri.domain.user.entity.UserType;
import ro.fmi.unibuc.licitatie_curieri.fixtures.UserFixtures;

import java.time.Instant;

@SpringBootTest(classes = {UserMapperImpl.class})
class UserMapperTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    void testMapToUserType() {
        Assertions.assertEquals(3, UserType.values().length);
        Assertions.assertEquals(3, UserTypeDto.values().length);
        
        Assertions.assertEquals(UserType.CLIENT, userMapper.mapToUserType(UserTypeDto.CLIENT));
        Assertions.assertEquals(UserType.COURIER, userMapper.mapToUserType(UserTypeDto.COURIER));
        Assertions.assertEquals(UserType.ADMIN_RESTAURANT, userMapper.mapToUserType(UserTypeDto.ADMIN_RESTAURANT));
    }

    @Test
    void testMapToUser() {
        val userCreationDto = UserFixtures.getUserCreationDtoFixture();

        val user = userMapper.mapToUser(userCreationDto);

        Assertions.assertNotNull(user);
        Assertions.assertNull(user.getId());
        Assertions.assertEquals(user.getFirstName(), userCreationDto.getFirstName());
        Assertions.assertEquals(user.getLastName(), userCreationDto.getLastName());
        Assertions.assertEquals(user.getEmail(), userCreationDto.getEmail());
        Assertions.assertEquals(user.getPhoneNumber(), userCreationDto.getPhoneNumber());
        Assertions.assertNotNull(user.getPassword());
        Assertions.assertNotEquals(user.getPassword(), userCreationDto.getPassword());
        Assertions.assertEquals(user.getUserType().name(), userCreationDto.getUserType().name());
        Assertions.assertNotNull(user.getEmailVerificationCode());
        Assertions.assertEquals(8, user.getEmailVerificationCode().length());
        Assertions.assertNotNull(user.getPhoneVerificationCode());
        Assertions.assertEquals(8, user.getPhoneVerificationCode().length());
        Assertions.assertNotNull(user.getVerificationDeadline());
        Assertions.assertTrue(Instant.now().isBefore(user.getVerificationDeadline()));
        Assertions.assertTrue(Instant.now().plusSeconds(301).isAfter(user.getVerificationDeadline()));
        Assertions.assertFalse(user.isVerified());
    }

    @Test
    void testMapToUserCreationResponseDto() {
        val user = UserFixtures.getUnverifiedUserFixture();

        val userCreationResponseDto = userMapper.mapToUserCreationResponseDto(user);

        Assertions.assertNotNull(userCreationResponseDto);
        Assertions.assertEquals(userCreationResponseDto.getId(), user.getId());
        Assertions.assertEquals(userCreationResponseDto.getFirstName(), user.getFirstName());
        Assertions.assertEquals(userCreationResponseDto.getLastName(), user.getLastName());
        Assertions.assertEquals(userCreationResponseDto.getEmail(), user.getEmail());
        Assertions.assertEquals(userCreationResponseDto.getPhoneNumber(), user.getPhoneNumber());
        Assertions.assertEquals(userCreationResponseDto.getUserType().name(), user.getUserType().name());
    }
}
