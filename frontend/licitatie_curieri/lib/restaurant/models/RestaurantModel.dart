import 'package:licitatie_curieri/address/models/AddressModel.dart';

import '../../address/providers/AddressProvider.dart';

class Restaurant {

  int id;
  String name;
  int addressId;
  Address? address;



  Restaurant({
    required this.id,
    required this.name,
    required this.addressId,

});

  factory Restaurant.fromJson(Map<String, dynamic> json)
  {
    return Restaurant(
        id: json["id"],
        name: json["name"],
        addressId: -1 // ???
    );
  }

  static Future<Restaurant> fromJsonWithAddress(Map<String, dynamic> json, AddressProvider addressProvider) async {
    double latitude = json["latitude"];
    double longitude = json["longitude"];

    Address? address = await addressProvider.fetchAddressFromCoordinates(latitude, longitude);
    if(address == null)
      {
        throw Exception("Couldn't find the specified address latitude: $latitude, longitude: $longitude");
      }

    Restaurant restaurant = Restaurant(
      id: json["id"],
      name: json["name"],
      addressId: address.id,
    );
    restaurant.address = address;
    return restaurant;
  }

  Map<String, dynamic> toJson() {
    return {
      "id":id,
      "name":name,
      "address":addressId,
    };
  }



}