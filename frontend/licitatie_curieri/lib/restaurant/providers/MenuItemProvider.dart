import 'package:flutter/cupertino.dart';
import 'package:licitatie_curieri/restaurant/services/MenuItemService.dart';

import '../models/MenuItemModel.dart';

class MenuItemProvider with ChangeNotifier {
  List<MenuItem> _menuItems = [];
  MenuItem? _selectedMenuItem;
  bool _isLoading = false;

  List<MenuItem> get menuItems => _menuItems;

  MenuItem? get selectedMenuItem => _selectedMenuItem;

  bool get isLoading => _isLoading;

  Future<void> fetchMenuItems(int restaurantId) async {
    _isLoading = true;
    notifyListeners();

    try {
      _menuItems =
          await MenuItemService().fetchMenuItemsByRestaurant(restaurantId);
    } catch (error) {
      print("Error fetching menuItems from Restaurant $restaurantId: $error");
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<void> fetchMenuItemById(int id) async {
    _isLoading = true;
    notifyListeners();

    try {
      _selectedMenuItem = await MenuItemService().fetchMenuItemById(id);
    } catch (error) {
      print("Error fetching menuItem $id: $error");
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<MenuItem> createMenuItem(MenuItem menuItem) async {
    _isLoading = true;
    notifyListeners();
    try {
      final _menuItem = await MenuItemService().createMenuItem(menuItem);
      return _menuItem;
    } catch (error) {
      print("Error creating restaurant: $error");
      rethrow;
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<void> updateMenuItem(int id, MenuItem updatedMenuItem) async {
    _isLoading = true;
    notifyListeners();
    try {
      await MenuItemService().updateMenuItem(id, updatedMenuItem);
    } catch (error) {
      print("Error updating restaurant $id: $error");
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<void> removeMenuItem(int id) async {
    _isLoading = true;
    notifyListeners();
    try {
      await MenuItemService().removeMenuItem(id);
    } catch (error) {
      print("Error removing restaurant $id: $error");
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }
}
