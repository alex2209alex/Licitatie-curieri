package ro.fmi.unibuc.licitatie_curieri.service;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.fmi.unibuc.licitatie_curieri.common.exception.ForbiddenException;
import ro.fmi.unibuc.licitatie_curieri.domain.address.mapper.AddressMapper;
import ro.fmi.unibuc.licitatie_curieri.domain.address.repository.AddressRepository;
import ro.fmi.unibuc.licitatie_curieri.domain.user.entity.UserType;
import ro.fmi.unibuc.licitatie_curieri.domain.user.repository.UserRepository;
import ro.fmi.unibuc.licitatie_curieri.fixtures.AddressFixtures;
import ro.fmi.unibuc.licitatie_curieri.fixtures.UserFixtures;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private AddressMapper addressMapper;

    @InjectMocks
    private AddressService addressService;

    @Test
    void givenUnverifiedUser_whenGetAddresses_thenForbiddenException() {
        val user = UserFixtures.getUnverifiedUserFixture();

        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(user));

        val exc = Assertions.assertThrows(ForbiddenException.class, () -> addressService.getAddresses());

        Assertions.assertEquals("User is unverified", exc.getMessage());
    }

    @Test
    void givenNonClientUser_whenGetAddresses_thenForbiddenException() {
        val user = UserFixtures.getVerifiedUserFixture();
        user.setUserType(UserType.COURIER);

        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(user));

        val exc = Assertions.assertThrows(ForbiddenException.class, () -> addressService.getAddresses());

        Assertions.assertEquals("Only client can get addresses", exc.getMessage());
    }

    @Test
    void givenVerifiedClientUser_whenGetAddresses_thenGetAddresses() {
        val user = UserFixtures.getVerifiedUserFixture();
        user.setUserType(UserType.CLIENT);
        user.setUserAddressAssociations(List.of(AddressFixtures.getUserAddressAssociationFixture(user)));
        val addressDetailsDto = AddressFixtures.getAddressDetailsDtoFixture();

        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(user));
        Mockito.when(addressMapper.mapToAddressDetailsDto(any())).thenReturn(addressDetailsDto);

        Assertions.assertDoesNotThrow(() -> addressService.getAddresses());

        Mockito.verify(addressMapper, Mockito.times(1)).mapToAddressDetailsDto(any());
    }

    @Test
    void givenUnverifiedUser_whenCreateAddress_thenForbiddenException() {
        val addressCreationDto = AddressFixtures.getAddressCreationDtoFixture();
        val user = UserFixtures.getUnverifiedUserFixture();

        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(user));

        val exc = Assertions.assertThrows(ForbiddenException.class, () -> addressService.createAddress(addressCreationDto));

        Assertions.assertEquals("User is unverified", exc.getMessage());

        Mockito.verify(addressRepository, Mockito.never()).save(any());
    }

    @Test
    void givenNonClientUser_whenCreateAddress_thenForbiddenException() {
        val addressCreationDto = AddressFixtures.getAddressCreationDtoFixture();
        val user = UserFixtures.getVerifiedUserFixture();
        user.setUserType(UserType.COURIER);

        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(user));

        val exc = Assertions.assertThrows(ForbiddenException.class, () -> addressService.createAddress(addressCreationDto));

        Assertions.assertEquals("Only client user can create address", exc.getMessage());

        Mockito.verify(addressRepository, Mockito.never()).save(any());
    }

    @Test
    void givenVerifiedClientUser_whenCreateAddress_thenCreateAddress() {
        val addressCreationDto = AddressFixtures.getAddressCreationDtoFixture();
        val user = UserFixtures.getVerifiedUserFixture();
        user.setUserType(UserType.CLIENT);
        val address = AddressFixtures.getAddressFixture();
        val previousNumberOfUserAddresses = user.getUserAddressAssociations().size();

        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(user));
        Mockito.when(addressMapper.mapToAddress(addressCreationDto)).thenReturn(address);
        Mockito.when(addressRepository.save(address)).thenReturn(address);

        Assertions.assertDoesNotThrow(() -> addressService.createAddress(addressCreationDto));

        Assertions.assertEquals(previousNumberOfUserAddresses + 1, user.getUserAddressAssociations().size());

        Mockito.verify(addressRepository, Mockito.times(1)).save(any());
    }
}
