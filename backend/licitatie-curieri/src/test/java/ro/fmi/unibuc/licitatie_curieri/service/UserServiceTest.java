//package ro.fmi.unibuc.licitatie_curieri.service;
//
//import lombok.val;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import ro.fmi.unibuc.licitatie_curieri.common.exception.BadRequestException;
//import ro.fmi.unibuc.licitatie_curieri.common.exception.ForbiddenException;
//import ro.fmi.unibuc.licitatie_curieri.domain.user.entity.UserType;
//import ro.fmi.unibuc.licitatie_curieri.domain.user.mapper.UserMapper;
//import ro.fmi.unibuc.licitatie_curieri.domain.user.repository.UserRepository;
//import ro.fmi.unibuc.licitatie_curieri.fixtures.UserFixtures;
//
//import java.time.Instant;
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.any;
//
//@ExtendWith(MockitoExtension.class)
//class UserServiceTest {
//    @Mock
//    UserRepository userRepository;
//
//    @Mock
//    UserMapper userMapper;
//
//    @InjectMocks
//    UserService userService;
//
//    @Test
//    void givenExistingVerifiedUser_whenCreateUser_thenBadRequestException() {
//        val userCreationDto = UserFixtures.getUserCreationDtoFixture();
//        val user = UserFixtures.getVerifiedUserFixture();
//
//        Mockito.when(userMapper.mapToUserType(userCreationDto.getUserType())).thenReturn(UserType.CLIENT);
//        Mockito.when(userRepository.findByEmailAndUserType(userCreationDto.getEmail(), UserType.CLIENT)).thenReturn(Optional.of(user));
//
//        val exc = Assertions.assertThrows(BadRequestException.class, () -> userService.createUser(userCreationDto));
//
//        Assertions.assertEquals("Email " + user.getEmail() + " is already used for user type " + UserType.CLIENT.name(), exc.getMessage());
//
//        Mockito.verify(userRepository, Mockito.never()).save(any());
//    }
//
//    @Test
//    void givenExistingUserAwaitingVerification_whenCreateUser_thenBadRequestException() {
//        val userCreationDto = UserFixtures.getUserCreationDtoFixture();
//        val user = UserFixtures.getUnverifiedUserFixture();
//
//        Mockito.when(userMapper.mapToUserType(userCreationDto.getUserType())).thenReturn(UserType.CLIENT);
//        Mockito.when(userRepository.findByEmailAndUserType(userCreationDto.getEmail(), UserType.CLIENT)).thenReturn(Optional.of(user));
//
//        val exc = Assertions.assertThrows(BadRequestException.class, () -> userService.createUser(userCreationDto));
//
//        Assertions.assertEquals("User with email " + user.getEmail() + " and user type " +UserType.CLIENT.name() + " is awaiting verification", exc.getMessage());
//
//        Mockito.verify(userRepository, Mockito.never()).save(any());
//    }
//
//    @Test
//    void givenExistingUserWithExpiredVerification_whenCreateUser_thenDeleteExpiredUserBeforeOtherVerification() {
//        val userCreationDto = UserFixtures.getUserCreationDtoFixture();
//        userCreationDto.setEmail("invalid");
//        val user = UserFixtures.getUnverifiedUserFixture();
//        user.setVerificationDeadline(Instant.now().minusSeconds(1));
//
//        Mockito.when(userMapper.mapToUserType(userCreationDto.getUserType())).thenReturn(UserType.CLIENT);
//        Mockito.when(userRepository.findByEmailAndUserType(userCreationDto.getEmail(), UserType.CLIENT)).thenReturn(Optional.of(user));
//
//        val exc = Assertions.assertThrows(BadRequestException.class, () -> userService.createUser(userCreationDto));
//
//        Assertions.assertEquals("Email is invalid", exc.getMessage());
//
//        Mockito.verify(userRepository, Mockito.times(1)).delete(user);
//        Mockito.verify(userRepository, Mockito.never()).save(any());
//    }
//
//    @Test
//    void givenInvalidEmail_whenCreateUser_thenBadRequestException() {
//        val userCreationDto = UserFixtures.getUserCreationDtoFixture();
//        userCreationDto.setEmail("invalid");
//
//        Mockito.when(userMapper.mapToUserType(userCreationDto.getUserType())).thenReturn(UserType.CLIENT);
//        Mockito.when(userRepository.findByEmailAndUserType(userCreationDto.getEmail(), UserType.CLIENT)).thenReturn(Optional.empty());
//
//        val exc = Assertions.assertThrows(BadRequestException.class, () -> userService.createUser(userCreationDto));
//
//        Assertions.assertEquals("Email is invalid", exc.getMessage());
//
//        Mockito.verify(userRepository, Mockito.never()).save(any());
//    }
//
//    @Test
//    void givenInvalidPhoneNumber_whenCreateUser_thenBadRequestException() {
//        val userCreationDto = UserFixtures.getUserCreationDtoFixture();
//        userCreationDto.setPhoneNumber("invalid");
//
//        Mockito.when(userMapper.mapToUserType(userCreationDto.getUserType())).thenReturn(UserType.CLIENT);
//        Mockito.when(userRepository.findByEmailAndUserType(userCreationDto.getEmail(), UserType.CLIENT)).thenReturn(Optional.empty());
//
//        val exc = Assertions.assertThrows(BadRequestException.class, () -> userService.createUser(userCreationDto));
//
//        Assertions.assertEquals("Phone number is invalid", exc.getMessage());
//
//        Mockito.verify(userRepository, Mockito.never()).save(any());
//    }
//
//    @Test
//    void givenNonMatchingPasswords_whenCreateUser_thenBadRequestException() {
//        val userCreationDto = UserFixtures.getUserCreationDtoFixture();
//        userCreationDto.setPassword(userCreationDto.getPasswordConfirmation() + "non matching password");
//
//        Mockito.when(userMapper.mapToUserType(userCreationDto.getUserType())).thenReturn(UserType.CLIENT);
//        Mockito.when(userRepository.findByEmailAndUserType(userCreationDto.getEmail(), UserType.CLIENT)).thenReturn(Optional.empty());
//
//        val exc = Assertions.assertThrows(BadRequestException.class, () -> userService.createUser(userCreationDto));
//
//        Assertions.assertEquals("Password is different from password confirmation", exc.getMessage());
//
//        Mockito.verify(userRepository, Mockito.never()).save(any());
//    }
//
//    @Test
//    void givenMatchingInvalidPasswords_whenCreateUser_thenBadRequestException() {
//        val userCreationDto = UserFixtures.getUserCreationDtoFixture();
//        userCreationDto.setPassword("invalid");
//        userCreationDto.setPasswordConfirmation("invalid");
//
//        Mockito.when(userMapper.mapToUserType(userCreationDto.getUserType())).thenReturn(UserType.CLIENT);
//        Mockito.when(userRepository.findByEmailAndUserType(userCreationDto.getEmail(), UserType.CLIENT)).thenReturn(Optional.empty());
//
//        val exc = Assertions.assertThrows(BadRequestException.class, () -> userService.createUser(userCreationDto));
//
//        Assertions.assertEquals("Password is invalid. It should contain at least one capital letter, one lower case letter, one digit and one special character (~!@#$%^&*()_+). It should not contain white spaces", exc.getMessage());
//
//        Mockito.verify(userRepository, Mockito.never()).save(any());
//    }
//
////    @Test
////    void givenValidUser_whenCreateUser_thenCreateUser() {
////        val userCreationDto = UserFixtures.getUserCreationDtoFixture();
////        val user = UserFixtures.getUnverifiedUserFixture();
////
////        Mockito.when(userMapper.mapToUserType(userCreationDto.getUserType())).thenReturn(UserType.CLIENT);
////        Mockito.when(userRepository.findByEmailAndUserType(userCreationDto.getEmail(), UserType.CLIENT)).thenReturn(Optional.empty());
////        Mockito.when(userMapper.mapToUser(userCreationDto)).thenReturn(user);
////
////        Assertions.assertDoesNotThrow(() -> userService.createUser(userCreationDto));
////
////        Mockito.verify(userRepository, Mockito.times(1)).save(user);
////    }
//
//    @Test
//    void givenNoUserIsFoundWithEmailAndCodes_whenVerifyUser_thenBadRequestException() {
//        val email = "email";
//        val userVerificationDto = UserFixtures.getUserVerificationDtoFixture();
//
//        Mockito.when(userRepository.findByEmailAndEmailVerificationCodeAndPhoneVerificationCode(email, userVerificationDto.getEmailVerificationCode(), userVerificationDto.getPhoneVerificationCode())).thenReturn(Optional.empty());
//
//        val exc = Assertions.assertThrows(BadRequestException.class, () -> userService.verifyUser(email, userVerificationDto));
//
//        Assertions.assertEquals("User verification failed", exc.getMessage());
//    }
//
//    @Test
//    void givenUserWithExpiredVerificationIsFound_whenVerifyUser_thenDeleteUserAndBadRequestException() {
//        val email = "email";
//        val userVerificationDto = UserFixtures.getUserVerificationDtoFixture();
//        val user = UserFixtures.getUnverifiedUserFixture();
//        user.setVerificationDeadline(Instant.now().minusSeconds(1));
//
//        Mockito.when(userRepository.findByEmailAndEmailVerificationCodeAndPhoneVerificationCode(email, userVerificationDto.getEmailVerificationCode(), userVerificationDto.getPhoneVerificationCode())).thenReturn(Optional.of(user));
//
//        val exc = Assertions.assertThrows(ForbiddenException.class, () -> userService.verifyUser(email, userVerificationDto));
//
//        Assertions.assertEquals("User verification failed. User was deleted", exc.getMessage());
//
//        Mockito.verify(userRepository, Mockito.times(1)).delete(user);
//    }
//
//    @Test
//    void givenUnverifiedUser_whenVerifyUser_thenVerifyUser() {
//        val email = "email";
//        val userVerificationDto = UserFixtures.getUserVerificationDtoFixture();
//        val user = UserFixtures.getUnverifiedUserFixture();
//        user.setVerificationDeadline(Instant.now().plusSeconds(300));
//        user.setEmailVerificationCode(userVerificationDto.getEmailVerificationCode());
//        user.setPhoneVerificationCode(userVerificationDto.getPhoneVerificationCode());
//
//        Mockito.when(userRepository.findByEmailAndEmailVerificationCodeAndPhoneVerificationCode(email, userVerificationDto.getEmailVerificationCode(), userVerificationDto.getPhoneVerificationCode())).thenReturn(Optional.of(user));
//
//        Assertions.assertDoesNotThrow(() -> userService.verifyUser(email, userVerificationDto));
//
//        Assertions.assertTrue(user.isVerified());
//        Assertions.assertNull(user.getEmailVerificationCode());
//        Assertions.assertNull(user.getPhoneVerificationCode());
//        Assertions.assertNull(user.getVerificationDeadline());
//    }
//}
