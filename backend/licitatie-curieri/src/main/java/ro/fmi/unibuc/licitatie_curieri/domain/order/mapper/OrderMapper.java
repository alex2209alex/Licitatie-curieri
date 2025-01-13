package ro.fmi.unibuc.licitatie_curieri.domain.order.mapper;

import lombok.val;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import ro.fmi.unibuc.licitatie_curieri.controller.order.models.*;
import ro.fmi.unibuc.licitatie_curieri.domain.address.entity.Address;
import ro.fmi.unibuc.licitatie_curieri.domain.order.entity.Order;
import ro.fmi.unibuc.licitatie_curieri.domain.order.entity.OrderStatus;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface OrderMapper {

    @Mapping(target = "restaurantAddress", source = ".", qualifiedByName = "getRestaurantAddress")
    @Mapping(target = "clientAddress", source = "address.details")
    @Mapping(target = "lowestBid", source = "deliveryPrice")
    @Mapping(target = "auctionDeadline", source = "auctionDeadline", qualifiedByName = "toOffsetDateTime")
    OrderDetailsDto mapToOrderDetailsDto(Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deliveryPrice", ignore = true)
    @Mapping(target = "auctionDeadline", expression = "java(this.getAuctionDeadline())")
    @Mapping(target = "orderStatus", expression = "java(this.getOrderStatusForCreation())")
    @Mapping(target = "courier", ignore = true)
    @Mapping(target = "orderMenuItemAssociations", ignore = true)
    Order mapToOrder(OrderCreationDto orderCreationDto, double foodPrice, Address address);

    @Mapping(target = "addressId", source = "address.id")
    @Mapping(target = "auctionDeadline", source = "auctionDeadline", qualifiedByName = "toOffsetDateTime")
    @Mapping(target = "deliveryPriceLimit", source = "deliveryPrice")
    @Mapping(target = "price", source = "foodPrice")
    @Mapping(target = "items", source = ".", qualifiedByName = "getOrderCreationItemResponseDtos")
    OrderCreationResponseDto mapToOrderCreationResponse(Order order);

    @Mapping(target = "deliveryAddress", source = "address")
    @Mapping(target = "items", source = ".", qualifiedByName = "getOrderItemDetailsDtos")
    OrderToDeliverDetailsDto mapToOrderToDeliverDetailsDto(Order order);

    default Instant getAuctionDeadline() {
        return Instant.now().plusSeconds(180);
    }

    default OrderStatus getOrderStatusForCreation() {
        return OrderStatus.IN_AUCTION;
    }

    @Named("getRestaurantAddress")
    default String getRestaurantAddress(Order order) {
        return order.getOrderMenuItemAssociations().getFirst().getMenuItem().getRestaurant().getAddress().getDetails();
    }

    @Named("toOffsetDateTime")
    default OffsetDateTime toOffsetDateTime(Instant instant) {
        return OffsetDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    @Named("getOrderCreationItemResponseDtos")
    default List<OrderCreationItemResponseDto> getOrderCreationItemResponseDtos(Order order) {
        return order.getOrderMenuItemAssociations().stream()
                .map(orderMenuItemAssociation -> {
                    val orderCreationItemResponseDto = new OrderCreationItemResponseDto();
                    orderCreationItemResponseDto.setId(orderMenuItemAssociation.getId().getMenuItemId());
                    orderCreationItemResponseDto.setQuantity(orderMenuItemAssociation.getQuantity());
                    return orderCreationItemResponseDto;
                })
                .toList();
    }

    @Named("getOrderItemDetailsDtos")
    default List<OrderItemDetailsDto> getOrderItemDetailsDtos(Order order) {
        return order.getOrderMenuItemAssociations().stream()
                .map(orderMenuItemAssociation -> {
                    val orderCreationItemResponseDto = new OrderItemDetailsDto();
                    orderCreationItemResponseDto.setId(orderMenuItemAssociation.getId().getMenuItemId());
                    orderCreationItemResponseDto.setName(orderMenuItemAssociation.getMenuItem().getName());
                    orderCreationItemResponseDto.setQuantity(orderMenuItemAssociation.getQuantity());
                    return orderCreationItemResponseDto;
                })
                .toList();
    }
}