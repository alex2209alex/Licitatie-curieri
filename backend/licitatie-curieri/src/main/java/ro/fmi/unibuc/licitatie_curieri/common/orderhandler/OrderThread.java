package ro.fmi.unibuc.licitatie_curieri.common.orderhandler;

import lombok.extern.slf4j.Slf4j;
import ro.fmi.unibuc.licitatie_curieri.common.exception.InternalServerErrorException;
import ro.fmi.unibuc.licitatie_curieri.common.utils.ErrorMessageUtils;

@Slf4j
public class OrderThread extends Thread {
    private final OrderHandler orderHandler;
    private final Long orderId;
    private final long delayInSeconds;

    public OrderThread(OrderHandler orderHandler, Long orderId, long delayInSeconds) {
        this.orderHandler = orderHandler;
        this.orderId = orderId;
        this.delayInSeconds = delayInSeconds;
    }

    @Override
    public void run() {
        log.info("Order thread started " + orderId + " " + delayInSeconds);
        try {
            Thread.sleep(delayInSeconds * 1000);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            throw new InternalServerErrorException(ErrorMessageUtils.ERROR_WITH_ORDER_THREAD);
        } finally {
            synchronized (OrderHandler.mutex) {
                orderHandler.checkIfOrderIsAwaitingPaymentOrCancelOrder(orderId);
            }
        }
    }
}
