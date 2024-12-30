import 'package:flutter/cupertino.dart';
import 'package:http/http.dart' as http;
import 'package:licitatie_curieri/address/providers/AddressProvider.dart';
import 'package:licitatie_curieri/restaurant/services/RestaurantService.dart';

import '../models/RestaurantModel.dart';

class RestaurantProvider with ChangeNotifier {
  List<Restaurant> _restaurants = [];
  Restaurant? _selectedRestaurant;
  bool _isLoading = false;

  List<Restaurant> get restaurants => _restaurants;
  Restaurant? get selectedRestaurant => _selectedRestaurant;
  bool get isLoading => _isLoading;

  set selectedRestaurant(Restaurant? restaurant) {
    _selectedRestaurant = restaurant;
    notifyListeners();
  }

  Future<void> fetchRestaurants() async {
    _isLoading = true;
    notifyListeners();
    try{
      _restaurants = await RestaurantService().fetchRestaurants();
    }catch(error) {
      print("Error fetching restaurants: $error");
    } finally{
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<void> fetchRestaurantsByUserIdForClientOnly(int id, AddressProvider addressProvider) async {
    _isLoading = true;
    notifyListeners();
    try {
      _restaurants = await RestaurantService().fetchRestaurantsByAddressIdForClientOnly(id, addressProvider);
    } catch (error) {
      print("Error fetching restaurants for address $id: $error");
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<http.Response> createRestaurant(Restaurant restaurant, AddressProvider addressProvider) async {
    _isLoading = true;
    notifyListeners();
    try {
      final response = await RestaurantService().createRestaurant(restaurant);

      if (response.statusCode == 201) {
        await fetchRestaurants();
      } else {
        print("Error: Failed to create restaurant. Status code: ${response.statusCode}");
      }
      return response;
    } catch (error) {
      print("Error creating restaurant: $error");
      rethrow;
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<void> updateRestaurant(int id, Restaurant updatedRestaurant) async {
    _isLoading = true;
    notifyListeners();
    try {
      await RestaurantService().updateRestaurant(id, updatedRestaurant);
      await fetchRestaurants();
    } catch (error) {
      print("Error updating restaurant $id: $error");
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<void> removeRestaurant(int id) async {
    _isLoading = true;
    notifyListeners();
    try {
      await RestaurantService().removeRestaurant(id);
      await fetchRestaurants();
    } catch(error)
    {
      print("Error removing restaurant $id: $error");
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  void setSelectedRestaurant(Restaurant restaurant) {
    _selectedRestaurant = restaurant;
    notifyListeners();
  }

}
