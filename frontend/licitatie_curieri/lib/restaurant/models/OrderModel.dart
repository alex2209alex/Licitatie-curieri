import 'package:licitatie_curieri/address/models/AddressModel.dart';

class Order {
  int id;
  String restaurantName;
  String deliveryAddress;
  double foodPrice;
  double deliveryPrice;
  String orderStatus;

  Order({
    required this.id,
    required this.restaurantName,
    required this.deliveryAddress,
    required this.foodPrice,
    required this.deliveryPrice,
    required this.orderStatus,
  });

  factory Order.fromJson(Map<String, dynamic> json)
  {
    return Order(
        id: json["id"],
        restaurantName: json["restaurantAddress"],
        deliveryAddress: json["clientAddress"],
        foodPrice: json["foodPrice"],
        deliveryPrice: json["deliveryPriceLimit"],
        orderStatus: json["orderStatus"],
    );
  }

  Map<String, dynamic> toUpdateJson() {
    return {
      "id":id,
      "restaurantName":restaurantName,
      "foodPrice":foodPrice,
      "deliveryPrice":deliveryPrice,
      "orderStatus":orderStatus,
    };
  }
}