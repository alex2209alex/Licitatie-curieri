package ro.fmi.unibuc.licitatie_curieri.controller;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ro.fmi.unibuc.licitatie_curieri.common.utils.LogMessageUtils;
import ro.fmi.unibuc.licitatie_curieri.controller.order.api.OrderApi;
import ro.fmi.unibuc.licitatie_curieri.controller.order.models.*;
import ro.fmi.unibuc.licitatie_curieri.service.OrderService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderController implements OrderApi {
    private final OrderService orderService;

    @Override
    public OrderCreationResponseDto createOrder(@RequestBody OrderCreationDto orderCreationDto) {
        log.info(String.format(LogMessageUtils.CREATE_ORDER));

        return orderService.createOrder(orderCreationDto);
    }
}