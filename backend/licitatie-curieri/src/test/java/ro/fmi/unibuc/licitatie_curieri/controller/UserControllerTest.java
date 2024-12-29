//package ro.fmi.unibuc.licitatie_curieri.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.SneakyThrows;
//import lombok.val;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.MethodSource;
//import org.mockito.Mockito;
//import org.openapitools.model.UserCreationDto;
//import org.openapitools.model.UserCreationResponseDto;
//import org.openapitools.model.UserTypeDto;
//import org.openapitools.model.UserVerificationDto;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import ro.fmi.unibuc.licitatie_curieri.common.GlobalExceptionHandler;
//import ro.fmi.unibuc.licitatie_curieri.common.exception.GenericApplicationError;
//import ro.fmi.unibuc.licitatie_curieri.fixtures.UserFixtures;
//import ro.fmi.unibuc.licitatie_curieri.service.UserService;
//
//import java.util.stream.Stream;
//
//import static org.mockito.ArgumentMatchers.any;
//
//@WebMvcTest(controllers = UserController.class)
//@ContextConfiguration(classes = {UserController.class, GlobalExceptionHandler.class})
//@AutoConfigureMockMvc(addFilters = false)
//class UserControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private UserService userService;
//
//    record InvalidInputParametersForCreate(String firstName, String lastName, String email, String phoneNumber,
//                                           String password, String passwordConfirmation, UserTypeDto userType,
//                                           String message) {
//    }
//
//    record InvalidInputParametersForVerification(String emailVerificationCode, String phoneVerificationCode,
//                                                 String message) {
//    }
//
//    @ParameterizedTest
//    @MethodSource("provideInvalidInputParametersForCreate")
//    @SneakyThrows
//    void givenInvalidCreationRequest_whenCreateUser_thenBadRequest(InvalidInputParametersForCreate input) {
//        val userCreationDto = new UserCreationDto(input.firstName, input.lastName, input.email, input.phoneNumber, input.password, input.passwordConfirmation, input.userType);
//
//        val response = mockMvc.perform(MockMvcRequestBuilders.post("/users/signup")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(userCreationDto)))
//                .andReturn();
//
//        val responseBody = objectMapper.readValue(response.getResponse().getContentAsString(), GenericApplicationError.class);
//
//        Assertions.assertNotNull(responseBody);
//        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), responseBody.getStatus());
//        Assertions.assertNotNull(responseBody.getDetail());
//        Assertions.assertTrue(responseBody.getDetail().contains(input.message));
//
//        Mockito.verify(userService, Mockito.never()).createUser(any());
//    }
//
//    @Test
//    @SneakyThrows
//    void givenValidCreationRequest_whenCreateUser_thenCreated() {
//        val userCreationDto = UserFixtures.getUserCreationDtoFixture();
//        val userCreationResponseDto = UserFixtures.getUserCreationResponseDtoFixture();
//
//        Mockito.when(userService.createUser(any())).thenReturn(userCreationResponseDto);
//
//        val response = mockMvc.perform(MockMvcRequestBuilders.post("/users/signup")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(userCreationDto)))
//                .andExpect(MockMvcResultMatchers.status().isCreated())
//                .andReturn();
//
//        val responseBody = objectMapper.readValue(response.getResponse().getContentAsString(), UserCreationResponseDto.class);
//
//        Assertions.assertNotNull(responseBody);
//        Assertions.assertEquals(responseBody.getId(), userCreationResponseDto.getId());
//        Assertions.assertEquals(responseBody.getFirstName(), userCreationResponseDto.getFirstName());
//        Assertions.assertEquals(responseBody.getLastName(), userCreationResponseDto.getLastName());
//        Assertions.assertEquals(responseBody.getEmail(), userCreationResponseDto.getEmail());
//        Assertions.assertEquals(responseBody.getPhoneNumber(), userCreationResponseDto.getPhoneNumber());
//        Assertions.assertEquals(responseBody.getUserType(), userCreationResponseDto.getUserType());
//
//        Mockito.verify(userService, Mockito.times(1)).createUser(any());
//    }
//
//    @ParameterizedTest
//    @MethodSource("provideInvalidInputParametersForVerification")
//    @SneakyThrows
//    void givenInvalidVerificationRequest_whenVerifyUser_thenBadRequest(InvalidInputParametersForVerification input) {
//        val userVerificationDto = new UserVerificationDto(input.emailVerificationCode, input.phoneVerificationCode);
//
//        val response = mockMvc.perform(MockMvcRequestBuilders.put("/users/test@gmail.com/verification")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(userVerificationDto)))
//                .andReturn();
//
//        val responseBody = objectMapper.readValue(response.getResponse().getContentAsString(), GenericApplicationError.class);
//
//        Assertions.assertNotNull(responseBody);
//        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), responseBody.getStatus());
//        Assertions.assertNotNull(responseBody.getDetail());
//        Assertions.assertTrue(responseBody.getDetail().contains(input.message));
//
//        Mockito.verify(userService, Mockito.never()).verifyUser(any(), any());
//    }
//
//    @Test
//    @SneakyThrows
//    void givenValidVerificationRequest_whenVerifyUser_thenOk() {
//        val userVerificationDto = UserFixtures.getUserVerificationDtoFixture();
//
//        Mockito.doNothing().when(userService).verifyUser(any(), any());
//
//        mockMvc.perform(MockMvcRequestBuilders.put("/users/test@gmail.com/verification")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(userVerificationDto)))
//                .andExpect(MockMvcResultMatchers.status().isOk());
//
//        Mockito.verify(userService, Mockito.times(1)).verifyUser(any(), any());
//    }
//
//    private static Stream<InvalidInputParametersForCreate> provideInvalidInputParametersForCreate() {
//        val firstName = "firstName";
//        val lastName = "lastName";
//        val email = "email";
//        val phoneNumber = "phoneNumber";
//        val password = "password";
//        val passwordConfirmation = "password";
//        val userType = UserTypeDto.CLIENT;
//        val stringOfLength7 = "1234567";
//        val stringOfLength51 = "012345678901234567890123456789012345678901234567890";
//
//        return Stream.of(
//                new InvalidInputParametersForCreate(null, lastName, email, phoneNumber, password, passwordConfirmation, userType, "firstName"),
//                new InvalidInputParametersForCreate(firstName, null, email, phoneNumber, password, passwordConfirmation, userType, "lastName"),
//                new InvalidInputParametersForCreate(firstName, lastName, null, phoneNumber, password, passwordConfirmation, userType, "email"),
//                new InvalidInputParametersForCreate(firstName, lastName, email, null, password, passwordConfirmation, userType, "phoneNumber"),
//                new InvalidInputParametersForCreate(firstName, lastName, email, phoneNumber, null, passwordConfirmation, userType, "password"),
//                new InvalidInputParametersForCreate(firstName, lastName, email, phoneNumber, password, null, userType, "passwordConfirmation"),
//                new InvalidInputParametersForCreate(firstName, lastName, email, phoneNumber, password, passwordConfirmation, null, "userType"),
//                new InvalidInputParametersForCreate(stringOfLength51, lastName, email, phoneNumber, password, passwordConfirmation, userType, "firstName"),
//                new InvalidInputParametersForCreate(firstName, stringOfLength51, email, phoneNumber, password, passwordConfirmation, userType, "lastName"),
//                new InvalidInputParametersForCreate(firstName, lastName, stringOfLength51, phoneNumber, password, passwordConfirmation, userType, "email"),
//                new InvalidInputParametersForCreate(firstName, lastName, email, stringOfLength51, password, passwordConfirmation, userType, "phoneNumber"),
//                new InvalidInputParametersForCreate(firstName, lastName, email, phoneNumber, stringOfLength51, passwordConfirmation, userType, "password"),
//                new InvalidInputParametersForCreate(firstName, lastName, email, phoneNumber, password, stringOfLength51, userType, "passwordConfirmation"),
//                new InvalidInputParametersForCreate(firstName, lastName, email, phoneNumber, stringOfLength7, passwordConfirmation, userType, "password"),
//                new InvalidInputParametersForCreate(firstName, lastName, email, phoneNumber, password, stringOfLength7, userType, "passwordConfirmation")
//        );
//    }
//
//    private static Stream<InvalidInputParametersForVerification> provideInvalidInputParametersForVerification() {
//        val emailVerificationCode = "12345678";
//        val phoneVerificationCode = "87654321";
//
//        return Stream.of(
//                new InvalidInputParametersForVerification(null, phoneVerificationCode, "emailVerificationCode"),
//                new InvalidInputParametersForVerification(emailVerificationCode, null, "phoneVerificationCode")
//        );
//    }
//}
