package ro.fmi.unibuc.licitatie_curieri.domain.order.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ro.fmi.unibuc.licitatie_curieri.domain.order.entity.Order;
import ro.fmi.unibuc.licitatie_curieri.domain.order.entity.OrderStatus;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.customer.id = :customerId")
    List<Order> findByCustomerId(Long customerId);

    @Query("SELECT o FROM Order o WHERE o.courier.id = :courierId")
    List<Order> findByCourierId(Long courierId);

    List<Order> findByStatus(OrderStatus status);
}
