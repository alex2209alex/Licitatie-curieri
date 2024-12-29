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
//import ro.fmi.unibuc.licitatie_curieri.common.exception.ForbiddenException;
//import ro.fmi.unibuc.licitatie_curieri.common.exception.NotFoundException;
//import ro.fmi.unibuc.licitatie_curieri.domain.restaurant.mapper.RestaurantMapper;
//import ro.fmi.unibuc.licitatie_curieri.domain.restaurant.repository.RestaurantRepository;
//import ro.fmi.unibuc.licitatie_curieri.domain.user.entity.UserType;
//import ro.fmi.unibuc.licitatie_curieri.domain.user.repository.UserRepository;
//import ro.fmi.unibuc.licitatie_curieri.fixtures.AddressFixtures;
//import ro.fmi.unibuc.licitatie_curieri.fixtures.RestaurantFixtures;
//import ro.fmi.unibuc.licitatie_curieri.fixtures.UserFixtures;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.any;
//
//@ExtendWith(MockitoExtension.class)
//class RestaurantServiceTest {
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private RestaurantRepository restaurantRepository;
//
//    @Mock
//    private RestaurantMapper restaurantMapper;
//
//    @InjectMocks
//    private RestaurantService restaurantService;
//
//    @Test
//    void givenUnverifiedUser_whenGetRestaurants_thenForbiddenException() {
//        val addressId = 1L;
//        val user = UserFixtures.getUnverifiedUserFixture();
//
//        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(user));
//
//        val exc = Assertions.assertThrows(ForbiddenException.class, () -> restaurantService.getRestaurants(addressId));
//
//        Assertions.assertEquals("User is unverified", exc.getMessage());
//    }
//
//    @Test
//    void givenNonClientUser_whenGetRestaurants_thenForbiddenException() {
//        val addressId = 1L;
//        val user = UserFixtures.getVerifiedUserFixture();
//        user.setUserType(UserType.COURIER);
//
//        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(user));
//
//        val exc = Assertions.assertThrows(ForbiddenException.class, () -> restaurantService.getRestaurants(addressId));
//
//        Assertions.assertEquals("Only client can get restaurants", exc.getMessage());
//    }
//
//    @Test
//    void givenAddressNotAssociatedToUser_whenGetRestaurants_thenNotFoundException() {
//        val addressId = 1L;
//        val user = UserFixtures.getVerifiedUserFixture();
//        user.setUserType(UserType.CLIENT);
//        user.setUserAddressAssociations(List.of());
//
//        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(user));
//
//        val exc = Assertions.assertThrows(NotFoundException.class, () -> restaurantService.getRestaurants(addressId));
//
//        Assertions.assertEquals("User address " + addressId + " not found", exc.getMessage());
//    }
//
//    @Test
//    void givenAddressBelongsToUser_whenGetRestaurants_thenGetRestaurantsInSearchArea() {
//        val addressId = 1L;
//        val user = UserFixtures.getVerifiedUserFixture();
//        user.setUserType(UserType.CLIENT);
//        val userAddressAssociations = AddressFixtures.getUserAddressAssociationFixture(user);
//        userAddressAssociations.getAddress().setId(addressId);
//        userAddressAssociations.getAddress().setLatitude(44.439663);
//        userAddressAssociations.getAddress().setLongitude(26.096306);
//        user.setUserAddressAssociations(List.of(userAddressAssociations));
//        val restaurant = RestaurantFixtures.getRestaurantFixture();
//        restaurant.setId(1L);
//        restaurant.getAddress().setLatitude(44.439663);
//        restaurant.getAddress().setLongitude(26.156306);
//        val restaurant2 = RestaurantFixtures.getRestaurantFixture();
//        restaurant2.setId(2L);
//        restaurant2.getAddress().setLatitude(45.439663);
//        restaurant2.getAddress().setLongitude(26.156306);
//        val restaurantDetailsDto = RestaurantFixtures.getRestaurantDetailsDto(restaurant);
//        val restaurantDetailsDto2 = RestaurantFixtures.getRestaurantDetailsDto(restaurant2);
//
//        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(user));
//        Mockito.when(restaurantRepository.findAll()).thenReturn(List.of(restaurant, restaurant2));
//        Mockito.when(restaurantMapper.toRestaurantDetailsDto(restaurant)).thenReturn(restaurantDetailsDto);
//        Mockito.when(restaurantMapper.toRestaurantDetailsDto(restaurant2)).thenReturn(restaurantDetailsDto2);
//
//        val restaurantDetailsDtos = restaurantService.getRestaurants(addressId);
//
//        Assertions.assertEquals(1, restaurantDetailsDtos.size());
//        Assertions.assertEquals(restaurantDetailsDto, restaurantDetailsDtos.getFirst());
//    }
//}
