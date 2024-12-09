import 'package:flutter/cupertino.dart';
import 'package:shared_preferences/shared_preferences.dart';

class CartProvider with ChangeNotifier {
  int _cartCounter = 0;

  int get cartCounter => _cartCounter;


  Future<void> setCounter(int value) async {
    final prefs = await SharedPreferences.getInstance();
    _cartCounter = value;
    prefs.setInt('cartCounter', value);

    notifyListeners();
  }

  Future<void> loadCounter() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    _cartCounter = prefs.getInt('counter') ?? 0;

    notifyListeners();
  }
}