import 'package:licitatie_curieri/restaurant/models/MenuItemModel.dart';
import 'package:licitatie_curieri/restaurant/models/RestaurantMenuItemModel.dart';

class Order {
  int id;

  Order({
    required this.id,
  });

  factory Order.fromJson(Map<String, dynamic> json) {
    return Order(
      id: json["id"],
    );
  }
}

class OrderDetails extends Order {
  String restaurantAddress;
  String clientAddress;
  double foodPrice;
  double deliveryPriceLimit;
  double lowestBid;
  DateTime auctionDeadline;
  String orderStatus;

  OrderDetails({
    required int id,
    required this.restaurantAddress,
    required this.clientAddress,
    required this.foodPrice,
    required this.deliveryPriceLimit,
    required this.lowestBid,
    required this.auctionDeadline,
    required this.orderStatus,
  }) : super(id: id);

  factory OrderDetails.fromJson(Map<String, dynamic> json) {
    return OrderDetails(
      id: json["id"],
      orderStatus: json["orderStatus"],
      restaurantAddress: json["restaurantAddress"] ?? "Unknown Restaurant",
      clientAddress: json["clientAddress"] ?? "Unknown Address",
      foodPrice: json["foodPrice"] ?? 0.0,
      deliveryPriceLimit: json["deliveryPriceLimit"] ?? 0.0,
      lowestBid: json["lowestBid"] ?? 0.0,
      auctionDeadline: DateTime.parse(json["auctionDeadline"])
    );
  }

  OrderDetails copyWith({
    int? id,
    String? orderStatus,
    String? restaurantAddress,
    String? clientAddress,
    double? foodPrice,
    double? deliveryPriceLimit,
    double? lowestBid,
    DateTime? auctionDeadline,
}){
    return OrderDetails(id: id ?? this.id,
        restaurantAddress: restaurantAddress ?? this.restaurantAddress,
        clientAddress: clientAddress ?? this.clientAddress,
        foodPrice: foodPrice ?? this.foodPrice,
        deliveryPriceLimit: deliveryPriceLimit ?? this.deliveryPriceLimit,
        lowestBid: lowestBid ?? this.lowestBid,
        auctionDeadline: auctionDeadline ?? this.auctionDeadline,
        orderStatus: orderStatus ?? this.orderStatus);
  }


}

class OrderCreation extends Order{
  int addressId;
  double deliveryPriceLimit;
  List<RestaurantMenuItem> items;

  OrderCreation({
   required id,
   required this.addressId,
   required this.deliveryPriceLimit,
   required this.items
}) : super(id: id);

  Map<String, dynamic> toJson()
  {
    return {
      "addressId" : this.addressId,
      "deliveryPriceLimit": this.deliveryPriceLimit,
      "items" : items.map( (item) => item.toJson()).toList(),
    };
  }

}
class OrderToDeliverDetails extends Order {
  String deliveryAddress;
  double foodPrice;
  double deliveryPrice;
  List<OrderItemDetails> items;

  OrderToDeliverDetails({
    required int id,
    required String orderStatus,
    required this.deliveryAddress,
    required this.foodPrice,
    required this.deliveryPrice,
    required this.items,

  }) : super(id: id);

  factory OrderToDeliverDetails.fromJson(Map<String, dynamic> json) {
    return OrderToDeliverDetails(
      id: json["id"],
      orderStatus: json["orderStatus"] ?? "UNKNOWN",
      deliveryAddress: json["deliveryAddress"]["details"] ?? "Unknown Address",
      foodPrice: json["foodPrice"] ?? 0.0,
      deliveryPrice: json["deliveryPrice"] ?? 0.0,
      items: (json["items"] as List<dynamic>)
          .map((item) => OrderItemDetails.fromJson(item))
          .toList(),
    );
  }

}

class OrderItemDetails {
  int id;
  String name;
  int quantity;

  OrderItemDetails({
    required this.id,
    required this.name,
    required this.quantity,
  });

  factory OrderItemDetails.fromJson(Map<String, dynamic> json) {
    return OrderItemDetails(
      id: json["id"],
      name: json["name"],
      quantity: json["quantity"],
    );
  }
}
