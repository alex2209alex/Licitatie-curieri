package ro.fmi.unibuc.licitatie_curieri.fixtures;

import lombok.val;
import org.openapitools.model.AddressCreationDto;
import org.openapitools.model.AddressCreationResponseDto;
import ro.fmi.unibuc.licitatie_curieri.domain.address.entity.Address;

public class AddressFixtures {
    private AddressFixtures(){
    }

    public static AddressCreationDto getAddressCreationDtoFixture() {
        return new AddressCreationDto("details", 12.345, 54.321);
    }

    public static AddressCreationResponseDto getAddressCreationResponseDtoFixture() {
        val addressCreationResponseDto = new AddressCreationResponseDto();
        addressCreationResponseDto.setId(1L);
        addressCreationResponseDto.setDetails("details");
        addressCreationResponseDto.setLatitude(12.345);
        addressCreationResponseDto.setLongitude(54.321);
        return addressCreationResponseDto;
    }

    public static Address getAddressFixture() {
        val address = new Address();
        address.setId(1L);
        address.setDetails("details");
        address.setLatitude(12.345);
        address.setLongitude(54.321);
        return address;
    }
}
