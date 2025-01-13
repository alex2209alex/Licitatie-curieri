import 'dart:convert';
import 'dart:developer';

import 'package:http/http.dart' as http;
import 'package:licitatie_curieri/address/providers/AddressProvider.dart';
import 'package:licitatie_curieri/restaurant/models/RestaurantModel.dart';

import '../../common/GetToken.dart';
import '../../common/Utils.dart';

class RestaurantService{
  static const String baseUrl = '${Utils.BASE_URL}';
  final GetToken getToken = GetToken();

  Future<List<Restaurant>> fetchRestaurants() async {
    log("fetchRestaurants invoked");
    String? token = await getToken.getToken();
    log("token $token");

    if (token == null) {
      throw Exception("Authentication token not found");
    }

    Uri uri = Uri.parse("$baseUrl/restaurants");
    final response = await http.get(
      uri,
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $token',
      },
    );

    if(response.statusCode == 200)
      {
        final List<dynamic> data = json.decode(response.body);
        return data.map((json) => Restaurant.fromJson(json)).toList();
      }
    else
      {
        throw Exception("Failed to fetch restaurants");
      }
  }

  // TO GET RESTAURANTS FOR CLIENT WITHIN AREA
  Future<List<Restaurant>> fetchRestaurantsByAddressIdForClientOnly(int id, AddressProvider addressProvider) async {
    String? token = await getToken.getToken();

    if (token == null) {
      throw Exception("Authentication token not found");
    }

    Uri uri = Uri.parse("$baseUrl/restaurants?address_id=$id");
    final response = await http.get(
      uri,
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $token',
      },
    );

    if(response.statusCode == 200)
      {
        final List<dynamic> data = json.decode(response.body);

        List<Restaurant> restaurants = [];

        for (var jsonData in data)
          {
            try{
              Restaurant restaurant = await Restaurant.fromJson(jsonData);
              restaurants.add(restaurant);
            }
            catch(e)
            {
              log("Error fetching address for restaurant : $e");
            }
          }
        return restaurants;
      }
    else
      {
        throw Exception("Failed to fetch restaurant $id");
      }
  }

  Future<http.Response> createRestaurant(Restaurant restaurant) async {
    String? token = await getToken.getToken();

    if (token == null) {
      throw Exception("Authentication token not found");
    }

    Uri uri = Uri.parse("$baseUrl/restaurants");
    final requestBody = {
      "name": restaurant.name,
      "addressDetails": restaurant.address.details,
      "latitude": restaurant.address.latitude,
      "longitude": restaurant.address.longitude
    };

    log(json.encode(requestBody).toString());

    final response = await http.post(
      uri,
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $token',
      },
      body: json.encode(requestBody),
    );

    return response;
  }

  Future<void> updateRestaurant(int restaurantId, Restaurant restaurant) async {
    String? token = await getToken.getToken();

    if (token == null) {
      throw Exception("Authentication token not found");
    }

    Uri uri = Uri.parse("$baseUrl/restaurants/$restaurantId");
    final response = await http.put(
      uri,
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $token',
      },
      body: json.encode(restaurant.toUpdateJson()),
    );

    log(json.encode("update restaurant ${restaurant.toUpdateJson()}"));
    if (response.statusCode != 200) {
      throw Exception("Failed to update restaurant");
    }
  }

  Future<void> removeRestaurant(int id) async {
    String? token = await getToken.getToken();

    if (token == null) {
      throw Exception("Authentication token not found");
    }

    Uri uri = Uri.parse("$baseUrl/restaurants/$id");
    final response = await http.delete(
      uri,
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $token',
      },
    );

    if (response.statusCode != 200) {
      throw Exception("Failed to delete restaurant code ${response.statusCode}");
    }
  }
}
