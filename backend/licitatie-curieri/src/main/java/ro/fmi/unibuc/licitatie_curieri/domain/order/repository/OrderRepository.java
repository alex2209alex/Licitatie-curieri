package ro.fmi.unibuc.licitatie_curieri.domain.order.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ro.fmi.unibuc.licitatie_curieri.domain.order.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
