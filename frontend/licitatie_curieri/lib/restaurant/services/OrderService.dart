import 'dart:convert';
import 'dart:developer';

import 'package:http/http.dart' as http;
import 'package:licitatie_curieri/address/providers/AddressProvider.dart';
import 'package:licitatie_curieri/restaurant/models/OrderModel.dart';

import '../../common/GetToken.dart';
import '../../common/Utils.dart';

class OrderService{
  static const String baseUrl = '${Utils.baseUrl}';
  final GetToken getToken = GetToken();

  Future<List<Order>> fetchOrders() async {
    log("fetchOrders invoked");
    String? token = await getToken.getToken();
    log("token $token");

    if (token == null) {
      throw Exception("Authentication token not found");
    }

    Uri uri = Uri.parse("$baseUrl/orders/client");
    final response = await http.get(
      uri,
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $token'
      },
    );

    if(response.statusCode == 200)
    {
      final List<dynamic> data = json.decode(response.body);
      return data.map((json) => Order.fromJson(json)).toList();
    }
    else
    {
      throw Exception("Failed to fetch orders");
    }
  }

  Future<void> cancelOrder(int id) async {
    String? token = await getToken.getToken();

    if (token == null) {
      throw Exception("Authentication token not found");
    }

    Uri uri = Uri.parse("$baseUrl/orders/$id/cancel");
    final response = await http.put(
      uri,
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $token',
      },
    );

    if (response.statusCode != 200) {
      throw Exception("Failed to cancel order code ${response.statusCode}");
    }
  }
}
