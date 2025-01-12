package ro.fmi.unibuc.licitatie_curieri.common.orderhandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ro.fmi.unibuc.licitatie_curieri.common.exception.InternalServerErrorException;
import ro.fmi.unibuc.licitatie_curieri.common.exception.NotFoundException;
import ro.fmi.unibuc.licitatie_curieri.common.utils.ErrorMessageUtils;
import ro.fmi.unibuc.licitatie_curieri.domain.order.entity.Order;
import ro.fmi.unibuc.licitatie_curieri.domain.order.entity.OrderStatus;
import ro.fmi.unibuc.licitatie_curieri.domain.order.repository.OrderRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderHandler {
    public static final Object mutex = new Object();

    private final OrderRepository orderRepository;

    @Transactional
    public void handleOrdersInAuctionOnApplicationStartup() {
        synchronized (mutex) {
            for (Order order : orderRepository.findAllByOrderStatus(OrderStatus.IN_AUCTION)) {
                if (order.getAuctionDeadline().isBefore(Instant.now())) {
                    order.setOrderStatus(OrderStatus.CANCELLED);
                } else {
                    val orderThread = new OrderThread(this, order.getId(), ChronoUnit.SECONDS.between(Instant.now(), order.getAuctionDeadline()));
                    orderThread.start();
                }
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void checkIfOrderIsAwaitingPaymentOrCancelOrder(Long orderId) {
        val order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(String.format(ErrorMessageUtils.ORDER_NOT_FOUND, orderId)));

        if (OrderStatus.IN_AUCTION == order.getOrderStatus() && order.getDeliveryPrice() == null) {
            log.info("Order canceled because auction time is over and no offer made");
            order.setOrderStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
        } else if (OrderStatus.IN_AUCTION == order.getOrderStatus()) {
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
                throw new InternalServerErrorException(ErrorMessageUtils.ERROR_WITH_ORDER_THREAD);
            } finally {
                synchronized (OrderHandler.mutex) {
                    cancelOrderIfUnpaid(orderId);
                }
            }
        }
    }

    private void cancelOrderIfUnpaid(Long orderId) {
        val order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(String.format(ErrorMessageUtils.ORDER_NOT_FOUND, orderId)));

        if (OrderStatus.IN_AUCTION == order.getOrderStatus()) {
            log.info("Order canceled because paying window is over");
            order.setOrderStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
        }
    }
}
