package ro.fmi.unibuc.licitatie_curieri.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
import ro.fmi.unibuc.licitatie_curieri.fixtures.RestaurantFixtures;
import ro.fmi.unibuc.licitatie_curieri.service.RestaurantService;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = RestaurantController.class)
@ContextConfiguration(classes = {RestaurantController.class, GlobalExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
class RestaurantControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RestaurantService restaurantService;

    @Test
    @SneakyThrows
    void givenRequestWithoutAddressId_whenGetRestaurants_thenBadRequest() {
        val response = mockMvc.perform(MockMvcRequestBuilders.get("/restaurants"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        val responseBody = objectMapper.readValue(response.getResponse().getContentAsString(), GenericApplicationError.class);

        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), responseBody.getStatus());
        Assertions.assertTrue(responseBody.getDetail().contains("address_id"));
    }

    @Test
    @SneakyThrows
    void givenValidRequest_whenGetRestaurants_thenGetRestaurants() {
        val addressId = 1L;
        val requestParameters = RestaurantFixtures.getEmptyRequestParametersFixture();
        requestParameters.set("address_id", String.valueOf(addressId));
        val restaurantDetailsDto = RestaurantFixtures.getRestaurantDetailsDto();

        Mockito.when(restaurantService.getRestaurants(addressId)).thenReturn(List.of(restaurantDetailsDto));

        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParams(requestParameters))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(restaurantDetailsDto.getId()))
                .andExpect(jsonPath("$[0].name").value(restaurantDetailsDto.getName()))
                .andExpect(jsonPath("$[0].latitude").value(restaurantDetailsDto.getLatitude()))
                .andExpect(jsonPath("$[0].longitude").value(restaurantDetailsDto.getLongitude()));
    }
}
