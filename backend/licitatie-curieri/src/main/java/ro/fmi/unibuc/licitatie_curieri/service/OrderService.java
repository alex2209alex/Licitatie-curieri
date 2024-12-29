//package ro.fmi.unibuc.licitatie_curieri.service;
//
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import ro.fmi.unibuc.licitatie_curieri.domain.order.entity.Order;
//import ro.fmi.unibuc.licitatie_curieri.domain.order.mapper.OrderMapper;
//import ro.fmi.unibuc.licitatie_curieri.domain.order.repository.OrderRepository;
//
//@Service
//@RequiredArgsConstructor
//public class OrderService {
//    private final OrderRepository orderRepository;
//    private final OrderMapper orderMapper;
//
//    @Transactional
//    public Order createOrder(CreateOrderDto createOrderDto) {
//        Order order = orderMapper.toOrder(createOrderDto);
//        return orderRepository.save(order);
//    }
//
//    public List<Order> getOrdersByCustomerId(Long customerId) {
//        return orderRepository.findByCustomerId(customerId);
//    }
//
//    public List<Order> getOrdersByCourierId(Long courierId) {
//        return orderRepository.findByCourierId(courierId);
//    }
//
//    public List<Order> getOrdersByStatus(OrderStatus status) {
//        return orderRepository.findByStatus(status);
//    }
//}