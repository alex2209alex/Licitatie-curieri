//package ro.fmi.unibuc.licitatie_curieri.domain.order.mapper;
//
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.ReportingPolicy;
//import org.openapitools.model.*;
//import ro.fmi.unibuc.licitatie_curieri.domain.order.entity.Order;
//
//@Mapper(
//        componentModel = "spring",
//        unmappedTargetPolicy = ReportingPolicy.ERROR
//)
//public interface OrderMapper {
//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "orderDateTime", expression = "java(java.time.LocalDateTime.now())")
//    Order toOrder(CreateOrderDto createOrderDto);
//
//    OrderDetailsDto toOrderDetailsDto(Order order);
//}