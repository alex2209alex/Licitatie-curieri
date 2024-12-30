import 'package:licitatie_curieri/address/models/AddressModel.dart';

class Restaurant {
  int id;
  String name;
  Address address;

  Restaurant({
    required this.id,
    required this.name,
    required this.address
});

  factory Restaurant.fromJson(Map<String, dynamic> json)
  {
    return Restaurant(
        id: json["id"],
        name: json["name"],
        address: Address(id: json["address"]["id"], details: json["address"]["details"], latitude: json["address"]["latitude"], longitude: json["address"]["longitude"])
    );
  }

  Map<String, dynamic> toUpdateJson() {
    return {
      "id":id,
      "name":name
    };
  }
}