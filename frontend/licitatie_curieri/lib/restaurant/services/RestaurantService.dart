import 'dart:convert';
import 'dart:developer';
import 'package:device_preview/device_preview.dart';
import 'package:licitatie_curieri/address/providers/AddressProvider.dart';
import 'package:licitatie_curieri/restaurant/models/RestaurantModel.dart';
import 'package:http/http.dart' as http;
import 'package:provider/provider.dart';

import '../../address/models/AddressModel.dart';

import '../../common/GetToken.dart';
import '../../common/Utils.dart';

class RestaurantService{
  static const String baseUrl = '${Utils.baseUrl}';
  final GetToken getToken = GetToken();

  // NOT WORKING
  Future<List<Restaurant>> fetchRestaurants() async {
    String? token = await getToken.getToken();

    if (token == null) {
      throw Exception("Authentication token not found");
    }

    Uri uri = Uri.parse(baseUrl);
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
  Future<List<Restaurant>> fetchRestaurantsByUserIdForClientOnly(int id, AddressProvider addressProvider) async {
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
              Restaurant restaurant = await Restaurant.fromJsonWithAddress(jsonData, addressProvider);
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

  Future<http.Response> createRestaurant(Restaurant restaurant, Address address) async {
    String? token = await getToken.getToken();

    if (token == null) {
      throw Exception("Authentication token not found");
    }

    Uri uri = Uri.parse("$baseUrl/CreateRestaurant");
    final requestBody = {
      "name": restaurant.name,
      "address": address.details,
      "latitude": address.latitude,
      "longitude": address.longitude
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

  Future<void> updateRestaurant(int id, Restaurant restaurant) async {
    String? token = await getToken.getToken();

    if (token == null) {
      throw Exception("Authentication token not found");
    }

    Uri uri = Uri.parse("$baseUrl/UpdateRestaurantName");
    final response = await http.put(
      uri,
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $token',
      },
      body: json.encode(restaurant.toJson()),
    );

    log(json.encode("update restaurant ${restaurant.toJson()}"));
    if (response.statusCode != 200) {
      throw Exception("Failed to update restaurant");
    }
  }

  Future<void> deleteRestaurant(int id) async {
    String? token = await getToken.getToken();

    if (token == null) {
      throw Exception("Authentication token not found");
    }

    Uri uri = Uri.parse("$baseUrl/DeleteRestaurant?restaurant_id=$id");
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

