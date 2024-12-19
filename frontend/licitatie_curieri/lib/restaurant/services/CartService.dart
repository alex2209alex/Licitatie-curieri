import 'dart:convert';
import 'package:licitatie_curieri/restaurant/models/RestaurantMenuItemModel.dart';
import 'package:shared_preferences/shared_preferences.dart';

class CartService {
  static const _cartKey = "cartItems";

  Future<void> saveCartItems(List<RestaurantMenuItem> items) async {
    final prefs = await SharedPreferences.getInstance();
    final itemsJson = items.map((item) => item.toJson()).toList();

    prefs.setString(_cartKey, jsonEncode(itemsJson));
  }

  Future<List<RestaurantMenuItem>> getCartItems() async {
    List<RestaurantMenuItem> cartItems = [];

    final prefs = await SharedPreferences.getInstance();
    final itemsString = prefs.getString(_cartKey);

    if (itemsString != null) {
      final itemsList = jsonDecode(itemsString) as List;
      cartItems = itemsList.map((json) => RestaurantMenuItem.fromJson(json)).toList();
    }

    return cartItems;
  }

  Future<int> addItemToCart(RestaurantMenuItem restaurantMenuItem) async {
    List<RestaurantMenuItem> cartItems = await getCartItems();
    cartItems.add(restaurantMenuItem);
    saveCartItems(cartItems);

    return cartItems.length;
  }

  Future<int> removeItemAtIndex(int index) async {
    List<RestaurantMenuItem> cartItems = await getCartItems();
    cartItems.removeAt(index);
    saveCartItems(cartItems);

    return cartItems.length;
  }
}
