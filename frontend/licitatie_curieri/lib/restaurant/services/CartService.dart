import 'dart:convert';
import 'package:licitatie_curieri/restaurant/models/MenuItemModel.dart';
import 'package:shared_preferences/shared_preferences.dart';

class CartService {
  static const _cartKey = "cartItems";

  Future<void> saveCartItems(List<MenuItem> items) async {
    final prefs = await SharedPreferences.getInstance();
    final itemsJson = items.map((item) => item.toJson()).toList();

    prefs.setString(_cartKey, jsonEncode(itemsJson));
  }

  Future<List<MenuItem>> getCartItems() async {
    List<MenuItem> cartItems = [];

    final prefs = await SharedPreferences.getInstance();
    final itemsString = prefs.getString(_cartKey);

    if (itemsString != null) {
      final itemsList = jsonDecode(itemsString) as List;
      cartItems = itemsList.map((json) => MenuItem.fromJson(json)).toList();
    }

    return cartItems;
  }

  Future<int> addItemToCart(MenuItem menuItem) async {
    List<MenuItem> cartItems = await getCartItems();
    cartItems.add(menuItem);
    saveCartItems(cartItems);

    return cartItems.length;
  }

  Future<int> removeItemAtIndex(int index) async {
    List<MenuItem> cartItems = await getCartItems();
    cartItems.removeAt(index);
    saveCartItems(cartItems);

    return cartItems.length;
  }
}
