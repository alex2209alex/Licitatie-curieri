package ro.fmi.unibuc.licitatie_curieri.domain.address.mapper;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ro.fmi.unibuc.licitatie_curieri.fixtures.AddressFixtures;

@SpringBootTest(classes = {AddressMapperImpl.class})
class AddressMapperTest {
    @Autowired
    private AddressMapper addressMapper;

    @Test
    void testMapToAddress() {
        val addressCreationDto = AddressFixtures.getAddressCreationDtoFixture();

        val address = addressMapper.mapToAddress(addressCreationDto);

        Assertions.assertNotNull(address);
        Assertions.assertNull(address.getId());
        Assertions.assertEquals(address.getDetails(), addressCreationDto.getDetails());
        Assertions.assertEquals(address.getLatitude(), addressCreationDto.getLatitude());
        Assertions.assertEquals(address.getLongitude(), addressCreationDto.getLongitude());
    }

    @Test
    void testMapToAddressCreationResponseDto() {
        val address = AddressFixtures.getAddressFixture();

        val addressCreationResponseDto = addressMapper.mapToAddressCreationResponseDto(address);

        Assertions.assertNotNull(addressCreationResponseDto);
        Assertions.assertEquals(addressCreationResponseDto.getId(), address.getId());
        Assertions.assertEquals(addressCreationResponseDto.getDetails(), address.getDetails());
        Assertions.assertEquals(addressCreationResponseDto.getLatitude(), address.getLatitude());
        Assertions.assertEquals(addressCreationResponseDto.getLongitude(), address.getLongitude());
    }
}
