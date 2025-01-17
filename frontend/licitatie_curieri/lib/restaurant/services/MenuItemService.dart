import 'dart:convert';
import 'dart:developer';

import 'package:http/http.dart' as http;
import 'package:licitatie_curieri/common/GetToken.dart';

import '../../common/Utils.dart';
import '../models/MenuItemModel.dart';

class MenuItemService {
  static const String baseUrl = "${Utils.BASE_URL}/menu-items";
  final GetToken getToken = GetToken();

  Future<List<MenuItem>> fetchMenuItems(int restaurantId) async {
    String? token = await getToken.getToken();
    Uri uri = Uri.parse("$baseUrl?restaurantId=$restaurantId");
    final response = await http.get(uri, headers: {
      'Content-Type': 'application/json',
      'Authorization': 'Bearer $token',
    });
    if (response.statusCode == 200) {
      final List<dynamic> data = json.decode(response.body);
      return data.map((json) => MenuItem.fromJson(json)).toList();
    } else {
      throw Exception(
          "Failed to fetch MenuItems from restaurant $restaurantId - ${response.statusCode}");
    }
  }

  Future<MenuItem> fetchMenuItemById(int menuItemId) async {
    String? token = await getToken.getToken();

    Uri uri = Uri.parse("$baseUrl/$menuItemId");
    final response = await http.get(
      uri,
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $token',
      },
    );
    if (response.statusCode == 200) {
      return MenuItem.fromJson(json.decode(response.body));
    } else {
      throw Exception("Failed to fetch MenuItem $menuItemId");
    }
  }

  Future<MenuItem> createMenuItem(MenuItem menuItem) async {
    String? token = await getToken.getToken();
    Uri uri = Uri.parse(baseUrl);
    log("createMenuItem body: ${json.encode(menuItem.toJson())}");
    final response = await http.post(uri,
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer $token',
        },
        body: json.encode(menuItem.toJson()));
    if (response.statusCode == 201) {
      return MenuItem.fromJson(json.decode(response.body));
    } else {
      throw Exception("Failed to create MenuItem");
    }
  }

  Future<MenuItem> updateMenuItem(int menuItemId, MenuItem menuItem) async {
    Uri uri = Uri.parse("$baseUrl/$menuItemId");
    String? token = await getToken.getToken();
    final response = await http.put(uri,
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer $token',
        },
        body: json.encode(menuItem.toJson()));

    if (response.statusCode == 200) {
      return MenuItem.fromJson(json.decode(response.body));
    } else {
      throw Exception("Failed to update MenuItem $menuItemId");
    }
  }

  Future<void> removeMenuItem(int menuItemId) async {
    String? token = await getToken.getToken();
    Uri uri = Uri.parse("$baseUrl/$menuItemId");

    final response = await http.delete(
      uri,
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $token',
      },
    );
    if (response.statusCode != 204) {
      throw Exception("Failed to delete MenuItem $menuItemId");
    }
  }
}
