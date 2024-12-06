package ro.fmi.unibuc.licitatie_curieri.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.openapitools.model.AddressCreationDto;
import org.openapitools.model.AddressCreationResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ro.fmi.unibuc.licitatie_curieri.common.GlobalExceptionHandler;
import ro.fmi.unibuc.licitatie_curieri.common.exception.GenericApplicationError;
import ro.fmi.unibuc.licitatie_curieri.fixtures.AddressFixtures;
import ro.fmi.unibuc.licitatie_curieri.service.AddressService;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(controllers = AddressController.class)
@ContextConfiguration(classes = {AddressController.class, GlobalExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
class AddressControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AddressService addressService;

    record InvalidInputParametersForCreate(String details, Double latitude, Double longitude, String message) {
    }

    @ParameterizedTest
    @MethodSource("provideInvalidInputParametersForCreate")
    @SneakyThrows
    void givenInvalidCreationRequest_whenCreateAddress_thenBadRequest(InvalidInputParametersForCreate input) {
        val addressCreationDto = new AddressCreationDto(input.details, input.latitude, input.longitude);

        val response = mockMvc.perform(MockMvcRequestBuilders.post("/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addressCreationDto)))
                .andReturn();

        val responseBody = objectMapper.readValue(response.getResponse().getContentAsString(), GenericApplicationError.class);

        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), responseBody.getStatus());
        Assertions.assertNotNull(responseBody.getDetail());
        Assertions.assertTrue(responseBody.getDetail().contains(input.message));

        Mockito.verify(addressService, Mockito.never()).createAddress(any());
    }

    @Test
    @SneakyThrows
    void givenValidCreationRequest_whenCreateUser_thenCreated() {
        val addressCreationDto = AddressFixtures.getAddressCreationDtoFixture();
        val addressCreationResponseDto = AddressFixtures.getAddressCreationResponseDtoFixture();

        Mockito.when(addressService.createAddress(any())).thenReturn(addressCreationResponseDto);

        val response = mockMvc.perform(MockMvcRequestBuilders.post("/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addressCreationDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        val responseBody = objectMapper.readValue(response.getResponse().getContentAsString(), AddressCreationResponseDto.class);

        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals(responseBody.getId(), addressCreationResponseDto.getId());
        Assertions.assertEquals(responseBody.getDetails(), addressCreationResponseDto.getDetails());
        Assertions.assertEquals(responseBody.getLatitude(), addressCreationResponseDto.getLatitude());
        Assertions.assertEquals(responseBody.getLongitude(), addressCreationResponseDto.getLongitude());

        Mockito.verify(addressService, Mockito.times(1)).createAddress(any());
    }

    private static Stream<InvalidInputParametersForCreate> provideInvalidInputParametersForCreate() {
        val details = "details";
        val latitude = 12.345;
        val longitude = 54.321;
        val stringOfLength101 = "01234567890123456789012345678901234567890123456789001234567890123456789012345678901234567890123456789";

        return Stream.of(
                new InvalidInputParametersForCreate(null, latitude, longitude, "details"),
                new InvalidInputParametersForCreate(details, null, longitude, "latitude"),
                new InvalidInputParametersForCreate(details, latitude, null, "longitude"),
                new InvalidInputParametersForCreate(stringOfLength101, latitude, longitude, "details"),
                new InvalidInputParametersForCreate(details, -90.01, longitude, "latitude"),
                new InvalidInputParametersForCreate(details, 90.01, longitude, "latitude"),
                new InvalidInputParametersForCreate(details, latitude, -180.01, "longitude"),
                new InvalidInputParametersForCreate(details, latitude, 180.01, "longitude")
        );
    }
}
