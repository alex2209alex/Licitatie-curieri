package ro.fmi.unibuc.licitatie_curieri.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ro.fmi.unibuc.licitatie_curieri.common.utils.LogMessageUtils;
import ro.fmi.unibuc.licitatie_curieri.controller.order.api.OrderApi;
import ro.fmi.unibuc.licitatie_curieri.controller.order.models.OrderCreationDto;
import ro.fmi.unibuc.licitatie_curieri.controller.order.models.OrderCreationResponseDto;
import ro.fmi.unibuc.licitatie_curieri.controller.order.models.OrderDetailsDto;
import ro.fmi.unibuc.licitatie_curieri.service.OrderService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderController implements OrderApi {
    private final OrderService orderService;

    @Override
    public List<OrderDetailsDto> getClientOrders() {
        log.info(String.format(LogMessageUtils.GET_CLIENT_ORDER));

        return orderService.getClientOrders();
    }

    @Override
    public List<OrderDetailsDto> getNearbyOrders(@RequestParam(value = "latitude") Double latitude, @RequestParam(value = "longitude") Double longitude) {
        return null;
    }

    @Override
    public OrderCreationResponseDto createOrder(@RequestBody OrderCreationDto orderCreationDto) {
        log.info(String.format(LogMessageUtils.CREATE_ORDER));

        return orderService.createOrder(orderCreationDto);
    }

    @Override
    public void cancelOrder(@PathVariable("order_id") Long orderId) {

    }
}