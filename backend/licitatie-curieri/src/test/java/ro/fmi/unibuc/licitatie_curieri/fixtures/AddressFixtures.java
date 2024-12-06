package ro.fmi.unibuc.licitatie_curieri.fixtures;

import lombok.val;
import org.openapitools.model.AddressCreationDto;
import ro.fmi.unibuc.licitatie_curieri.domain.address.entity.Address;

public class AddressFixtures {
    private AddressFixtures(){
    }

    public static AddressCreationDto getAddressCreationDtoFixture() {
        val addressCreationDto = new AddressCreationDto();
        addressCreationDto.setDetails("details");
        addressCreationDto.setLatitude(12.345);
        addressCreationDto.setLongitude(54.321);
        return addressCreationDto;
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
