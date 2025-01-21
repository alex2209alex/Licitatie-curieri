package ro.fmi.unibuc.licitatie_curieri.domain.order.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ro.fmi.unibuc.licitatie_curieri.domain.order.entity.Order;
import ro.fmi.unibuc.licitatie_curieri.domain.order.entity.OrderStatus;

import java.util.List;
import java.util.Set;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.address.id IN :addressIds")
    List<Order> findAllByAddressIds(Set<Long> addressIds);

    List<Order> findAllByOrderStatus(OrderStatus orderStatus);
}
