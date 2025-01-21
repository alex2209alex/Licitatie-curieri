import 'dart:convert';
import 'dart:developer';

import 'package:geolocator/geolocator.dart';
import 'package:http/http.dart' as http;
import 'package:licitatie_curieri/address/providers/AddressProvider.dart';
import 'package:licitatie_curieri/restaurant/models/OrderModel.dart';
import 'package:licitatie_curieri/restaurant/models/RestaurantMenuItemModel.dart';
import 'package:licitatie_curieri/restaurant/providers/CartProvider.dart';
import 'package:licitatie_curieri/restaurant/services/CartService.dart';

import '../../common/GetToken.dart';
import '../../common/Utils.dart';

class OrderService {
  static const String baseUrl = '${Utils.BASE_URL}';
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

    if (response.statusCode == 200) {
      final List<dynamic> data = json.decode(response.body);
      log("Client Orders: " +
          data.map((json) => OrderDetails.fromJson(json)).toList().toString());
      return data.map((json) => OrderDetails.fromJson(json)).toList();
    } else {
      throw Exception("Failed to fetch orders");
    }
  }

  Future<OrderToDeliverDetails> fetchOrderDetails(int orderId) async {
    try {
      log("getOrderDetails invoked for order ID: $orderId");

      String? token = await getToken.getToken();
      if (token == null) {
        throw Exception("Authentication token not found");
      }

      Uri uri = Uri.parse("$baseUrl/orders/courier/$orderId");

      final response = await http.get(
        uri,
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer $token',
        },
      );

      // Handle the response
      if (response.statusCode == 200) {
        final Map<String, dynamic> data = json.decode(response.body);
        log("Order details fetched successfully: $data");
        return OrderToDeliverDetails.fromJson(data);
      } else {
        throw Exception(
            "Failed to fetch order details: ${response.statusCode}");
      }
    } catch (error) {
      log("Error fetching order details: $error");
      rethrow;
    }
  }

  Future<bool> createOrder(AddressProvider addressProvider,
      double deliveryPriceLimit, CartProvider cartProvider) async {
    log("fetchOrders invoked");
    String? token = await getToken.getToken();
    log("token $token");

    if (token == null) {
      throw Exception("Authentication token not found");
    }
    int? addressId = addressProvider.selectedAddressId;
    if (addressId == null) {
      throw Exception("Address is not selected");
    }

    List<RestaurantMenuItem> items = await CartService().getCartItems();
    if (items.isEmpty) {
      throw Exception("Must add items to cart");
    }

    OrderCreation orderCreation = OrderCreation(
        id: 0,
        addressId: addressId,
        deliveryPriceLimit: deliveryPriceLimit,
        items: items);
    final requestBody = orderCreation.toJson();

    Uri uri = Uri.parse("$baseUrl/orders/client");
    final response = await http.post(
      uri,
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $token',
      },
      body: json.encode(requestBody),
    );

    if (response.statusCode == 200) {
      final List<dynamic> data = json.decode(response.body);
      return true;
    } else {
      throw Exception("Failed statusCode for creation");
    }
  }

  Future<List<Order>> fetchOrdersCourier() async {
    try {
      log("fetchOrdersCourier invoked");

      String? token = await getToken.getToken();
      if (token == null) {
        throw Exception("Authentication token not found");
      }

      Position position = await _determinePosition();
      double latitude = position.latitude;
      double longitude = position.longitude;

      log("Current location: Latitude: $latitude, Longitude: $longitude");

      Uri uri = Uri.parse(
          "$baseUrl/orders/nearby?latitude=$latitude&longitude=$longitude");
      final response = await http.get(
        uri,
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer $token'
        },
      );
      log("respose fetchOrdersCourier: ${response.statusCode}");
      if (response.statusCode == 200) {
        final List<dynamic> data = json.decode(response.body);
        log(data.toString());
        return data.map((json) => OrderDetails.fromJson(json)).toList();
      } else {
        throw Exception("Failed to fetch orders: ${response.statusCode}");
      }
    } catch (error) {
      log("Error fetching orders: $error");
      rethrow; // Re-throw the error to be handled by the caller
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

  Future<Position> _determinePosition() async {
    bool serviceEnabled;
    LocationPermission permission;

    try {
      serviceEnabled = await Geolocator.isLocationServiceEnabled();
      if (!serviceEnabled) {
        throw Exception("Location services are disabled.");
      }

      permission = await Geolocator.checkPermission();
      if (permission == LocationPermission.denied) {
        permission = await Geolocator.requestPermission();
        if (permission == LocationPermission.denied) {
          throw Exception("Location permissions are denied.");
        }
      }

      if (permission == LocationPermission.deniedForever) {
        throw Exception(
            "Location permissions are permanently denied. We cannot request permissions.");
      }

      return await Geolocator.getCurrentPosition();
    } catch (e) {
      log("Error determining position: $e");
      rethrow;
    }
  }
}
