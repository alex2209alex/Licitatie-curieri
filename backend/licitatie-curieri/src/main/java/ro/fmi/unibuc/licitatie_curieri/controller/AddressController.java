package ro.fmi.unibuc.licitatie_curieri.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.AddressApi;
import org.openapitools.model.AddressCreationDto;
import org.openapitools.model.AddressCreationResponseDto;
import org.openapitools.model.AddressDetailsDto;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ro.fmi.unibuc.licitatie_curieri.common.utils.LogMessageUtils;
import ro.fmi.unibuc.licitatie_curieri.service.AddressService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AddressController implements AddressApi {
    private final AddressService addressService;

    @Override
    public List<AddressDetailsDto> getAddresses() {
        log.info(LogMessageUtils.GET_ADDRESSES);
        return addressService.getAddresses();
    }

    @Override
    public AddressCreationResponseDto createAddress(@RequestBody AddressCreationDto addressCreationDto) {
        log.info(LogMessageUtils.CREATE_ADDRESS);
        return addressService.createAddress(addressCreationDto);
    }
}
