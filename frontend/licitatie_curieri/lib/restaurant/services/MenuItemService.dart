import 'dart:convert';
import 'package:http/http.dart' as http;
import '../../common/GetToken.dart';
import '../../common/Utils.dart';
import '../models/MenuItemModel.dart';
class MenuItemService {

  static const String baseUrl = '${Utils.baseUrl}/MenuItems';
  final GetToken getToken = GetToken();

  Future<List<MenuItem>> fetchMenuItemsByRestaurant(int idRestaurant) async {
    String? token = await getToken.getToken();

    if (token == null) {
      throw Exception("Authentication token not found");
    }

    // To Do: later check the path for api
    Uri uri = Uri.parse("$baseUrl/restaurant/$idRestaurant");
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
        return data.map( (json) => MenuItem.fromJson(json)).toList();
      }
    else
      {
        throw Exception("Failed to fetch MenuItems from restaurant $idRestaurant");
      }
  }


  Future<MenuItem> fetchMenuItemById(int idMenu) async {
    String? token = await getToken.getToken();

    if (token == null) {
      throw Exception("Authentication token not found");
    }

    // To Do: later check the path for api
    Uri uri = Uri.parse("$baseUrl/$idMenu");
    final response = await http.get(
      uri,
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $token',
      },
    );

    if(response.statusCode == 200)
      {
        return MenuItem.fromJson(json.decode(response.body));
      }
    else
      {
        throw Exception("Failed to fetch MenuItem $idMenu");
      }
  }

  Future<MenuItem> createMenuItem(MenuItem menuItem) async {
    String? token = await getToken.getToken();

    if (token == null) {
      throw Exception("Authentication token not found");
    }

    // To Do: later check the path for api
    Uri uri = Uri.parse(baseUrl);
    final response = await http.post(
      uri,
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $token',
      },
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
    String? token = await getToken.getToken();

    if (token == null) {
      throw Exception("Authentication token not found");
    }

    // To Do: later check the path for api
    Uri uri = Uri.parse("$baseUrl/$id");

    final response = await http.put(
      uri,
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $token',
      },
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
    String? token = await getToken.getToken();

    if (token == null) {
      throw Exception("Authentication token not found");
    }

    // To Do: later check the path for api
    Uri uri = Uri.parse("$baseUrl/$id");
    final response = await http.delete(
      uri,
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $token',
      },
    );

    if(response.statusCode != 204)
      {
        throw Exception("Failed to delete MenuItem $id");
      }
  }
}