//package ro.fmi.unibuc.licitatie_curieri.fixtures;
//
//import lombok.val;
//import org.openapitools.model.AddressCreationDto;
//import org.openapitools.model.AddressCreationResponseDto;
//import org.openapitools.model.AddressDetailsDto;
//import ro.fmi.unibuc.licitatie_curieri.domain.address.entity.Address;
//import ro.fmi.unibuc.licitatie_curieri.domain.user.entity.User;
//import ro.fmi.unibuc.licitatie_curieri.domain.user.entity.UserAddressAssociation;
//import ro.fmi.unibuc.licitatie_curieri.domain.user.entity.UserAddressAssociationId;
//
//public class AddressFixtures {
//    private AddressFixtures(){
//    }
//
//    public static UserAddressAssociation getUserAddressAssociationFixture(User user) {
//        val userAddressAssociation = new UserAddressAssociation();
//        userAddressAssociation.setUser(user);
//        val address = getAddressFixture();
//        userAddressAssociation.setAddress(address);
//        val userAddressAssociationId = new UserAddressAssociationId();
//        userAddressAssociationId.setAddressId(address.getId());
//        userAddressAssociationId.setUserId(user.getId());
//        userAddressAssociation.setId(userAddressAssociationId);
//        return userAddressAssociation;
//    }
//
//    public static AddressDetailsDto getAddressDetailsDtoFixture() {
//        val addressDetailsDto = new AddressDetailsDto();
//        addressDetailsDto.setId(1L);
//        addressDetailsDto.setDetails("details");
//        addressDetailsDto.setLatitude(12.345);
//        addressDetailsDto.setLongitude(54.321);
//        return addressDetailsDto;
//    }
//
//    public static AddressCreationDto getAddressCreationDtoFixture() {
//        return new AddressCreationDto("details", 12.345, 54.321);
//    }
//
//    public static AddressCreationResponseDto getAddressCreationResponseDtoFixture() {
//        val addressCreationResponseDto = new AddressCreationResponseDto();
//        addressCreationResponseDto.setId(1L);
//        addressCreationResponseDto.setDetails("details");
//        addressCreationResponseDto.setLatitude(12.345);
//        addressCreationResponseDto.setLongitude(54.321);
//        return addressCreationResponseDto;
//    }
//
//    public static Address getAddressFixture() {
//        val address = new Address();
//        address.setId(1L);
//        address.setDetails("details");
//        address.setLatitude(12.345);
//        address.setLongitude(54.321);
//        return address;
//    }
//}
