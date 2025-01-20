package ro.fmi.unibuc.licitatie_curieri.controller.realtime.models;

import lombok.*;

@Getter
@Setter
public class OfferDto {
    private String token;
    private Long orderId;
    private Double deliveryPrice;
}
