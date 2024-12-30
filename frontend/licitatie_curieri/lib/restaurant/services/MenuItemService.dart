import 'dart:convert';
import 'package:http/http.dart' as http;
import '../../common/GetToken.dart';
import '../../common/Utils.dart';
import '../models/MenuItemModel.dart';
import 'dart:developer';

class MenuItemService {

  static const String baseUrl = '${Utils.baseUrl}';
  final GetToken getToken = GetToken();

  Future<List<MenuItem>> fetchMenuItemsByRestaurant(int idRestaurant) async {
    String? token = await getToken.getToken();

    if (token == null) {
      throw Exception("Authentication token not found");
    }

    Uri uri = Uri.parse("$baseUrl/GetAllMenusByRestaurantId?restaurant_id=$idRestaurant");
    final response = await http.get(
      uri,
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $token',
      },
    );

    log('Status code: ${response.statusCode}');
    log('URI: $uri');
    log('Headers: ${json.encode({
      'Content-Type': 'application/json',
      'Authorization': 'Bearer $token',
    })}');

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

    Uri uri = Uri.parse("$baseUrl/GetMenuById?menu_id=$idMenu");
    final response = await http.get(
      uri,
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $token',
      },
    );

    log('URI: $uri');
    log('Headers: ${json.encode({
      'Content-Type': 'application/json',
      'Authorization': 'Bearer $token',
    })}');

    if(response.statusCode == 200)
      {
        return MenuItem.fromJson(json.decode(response.body));
      }
    else
      {
        throw Exception("Failed to fetch MenuItem $idMenu");
      }
  }

  Future<http.Response> createMenuItem(MenuItem menuItem) async {
    String? token = await getToken.getToken();

    if (token == null) {
      throw Exception("Authentication token not found");
    }

    Uri uri = Uri.parse("$baseUrl/CreateMenu");
    final requestBody = {
      "idRestaurant": menuItem.idRestaurant,
      "name": menuItem.name,
      "price": menuItem.price,
      "ingredientsList": menuItem.ingredientsList,
      "photo": menuItem.photo,
      "discount": menuItem.discount,
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

    log('URI: $uri');
    log('Headers: ${json.encode({
      'Content-Type': 'application/json',
      'Authorization': 'Bearer $token',
    })}');
    log('Body: [${json.encode(requestBody)}]');

    return response;
  }

  Future<MenuItem> updateMenuItem(int id, MenuItem menuItem) async {
    String? token = await getToken.getToken();

    if (token == null) {
      throw Exception("Authentication token not found");
    }

    // To Do: later check the path for api
    Uri uri = Uri.parse("$baseUrl/UpdateMenu");

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

    Uri uri = Uri.parse("$baseUrl/DeleteMenu?menu_id=$id");
    final response = await http.delete(
      uri,
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $token',
      },
    );

    if(response.statusCode != 200)
      {
        throw Exception("Failed to delete MenuItem $id");
      }
  }
}