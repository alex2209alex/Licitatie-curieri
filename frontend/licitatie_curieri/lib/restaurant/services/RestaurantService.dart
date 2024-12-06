import 'dart:convert';
import 'package:licitatie_curieri/restaurant/models/RestaurantModel.dart';
import 'package:http/http.dart' as http;

class RestaurantService{

  static const String baseUrl = "localhost:8080/restaurants";

  Future<List<Restaurant>> fetchRestaurants() async {

    Uri uri = Uri.parse(baseUrl);
    final response = await http.get(uri);
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


  Future<Restaurant> fetchRestaurantById(int id) async {
    Uri uri = Uri.parse("$baseUrl/$id");
    final response = await http.get(uri);

    if(response.statusCode == 200)
      {
        return Restaurant.fromJson(json.decode(response.body));
      }
    else
      {
        throw Exception("Failed to fetch restaurant $id");
      }
  }
}