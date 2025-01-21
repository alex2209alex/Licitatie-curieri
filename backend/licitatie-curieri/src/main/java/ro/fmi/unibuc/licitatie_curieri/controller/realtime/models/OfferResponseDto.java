package ro.fmi.unibuc.licitatie_curieri.controller.realtime.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OfferResponseDto {
    private OfferStatusDto offerStatus;
    private Long orderId;
    private Double deliveryPrice;
}
