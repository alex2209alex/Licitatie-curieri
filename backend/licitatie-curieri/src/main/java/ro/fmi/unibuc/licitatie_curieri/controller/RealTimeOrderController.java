package ro.fmi.unibuc.licitatie_curieri.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import ro.fmi.unibuc.licitatie_curieri.controller.realtime.models.OfferDto;
import ro.fmi.unibuc.licitatie_curieri.controller.realtime.models.OfferResponseDto;
import ro.fmi.unibuc.licitatie_curieri.service.OrderService;

@Controller
@RequiredArgsConstructor
public class RealTimeOrderController {
    private final OrderService orderService;

    @MessageMapping("/order/makeOffer")
    @SendTo("/topic/offers")
    public OfferResponseDto makeOffer(OfferDto offerDto) {
        return orderService.makeOffer(offerDto);
    }
}
