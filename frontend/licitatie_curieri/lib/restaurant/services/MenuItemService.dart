import 'dart:convert';
import 'package:http/http.dart' as http;
import '../models/MenuItemModel.dart';
class MenuItemService {

  static const String baseUrl = "http://192.168.100.97:8080/MenuItems";

  Future<List<MenuItem>> fetchMenuItemsByRestaurant(int id) async {

    // To Do: later check the path for api
    Uri uri = Uri.parse("$baseUrl/restaurant/$id");
    final response = await http.get(uri);
    if(response.statusCode == 200)
      {
        final List<dynamic> data = json.decode(response.body);
        return data.map( (json) => MenuItem.fromJson(json)).toList();
      }
    else
      {
        throw Exception("Failed to fetch MenuItems from restaurant $id");
      }
  }


  Future<MenuItem> fetchMenuItemById(int id) async {
    // To Do: later check the path for api
    Uri uri = Uri.parse("$baseUrl/$id");
    final response = await http.get(uri);
    if(response.statusCode == 200)
      {
        return MenuItem.fromJson(json.decode(response.body));
      }
    else
      {
        throw Exception("Failed to fetch MenuItem $id");
      }
  }

Future<MenuItem> createMenuItem(MenuItem menuItem) async {
  // To Do: later check the path for api
  Uri uri = Uri.parse(baseUrl);
  final response = await http.post(
    uri,
    headers: {'Content-Type': 'application/json' },
    body: json.encode(menuItem.toJson())
  );
  if(response.statusCode == 200)
    {
      return MenuItem.fromJson(json.decode(response.body));
    }
  else
    {
      throw Exception("Failed to create MenuItem");
    }
}

Future<MenuItem> updateMenuItem(int id, MenuItem menuItem) async {
  // To Do: later check the path for api
  Uri uri = Uri.parse("$baseUrl/$id");

  final response = await http.put(
    uri,
    headers: {'Content-Type': 'application/json' },
    body: json.encode(menuItem.toJson())
  );

  if(response.statusCode == 200)
    {

      return MenuItem.fromJson(json.decode(response.body));
    }
  else
    {
      throw Exception("Failed to update MenuItem $id");
    }
}


Future<void> deleteMenuItem(int id) async {
  // To Do: later check the path for api
  Uri uri = Uri.parse("$baseUrl/$id");

  final response = await http.delete(uri);
  if(response.statusCode != 204)
    {
      throw Exception("Failed to delete MenuItem $id");
    }


}


}