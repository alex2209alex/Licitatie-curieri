import 'dart:convert';

import 'package:flutter/cupertino.dart';
import 'package:http/http.dart' as http;
import 'package:licitatie_curieri/address/models/AddressModel.dart';
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

      //  To do: Remove once fetching works. TO-DO: WIP: W.I.P.:
      initWithoutBackEnd();

      notifyListeners();
    }
  }

  Future<void> fetchRestaurantsByUserIdForClientOnly(int id, AddressProvider addressProvider) async {
    _isLoading = true;
    notifyListeners();
    try {
      _restaurants = await RestaurantService().fetchRestaurantsByUserIdForClientOnly(id, addressProvider);
    } catch (error) {
      print("Error fetching restaurants for address $id: $error");
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<http.Response> createRestaurant(Restaurant restaurant, Address address, AddressProvider addressProvider) async {
    _isLoading = true;
    notifyListeners();
    try {
      final response = await RestaurantService().createRestaurant(restaurant, address);

      if (response.statusCode == 201) {
        final data = json.decode(response.body);
        final createdAddress = await addressProvider.fetchAddressFromCoordinates(address.latitude, address.longitude);
        final createdRestaurant = Restaurant(id: data["id"], name: data["name"], addressId: createdAddress!.id);
        createdRestaurant.address = createdAddress;
        _restaurants.add(createdRestaurant);
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
      final index = _restaurants.indexWhere((r) => r.id == id);
      if (index != -1) {
        _restaurants[index] = updatedRestaurant;
      }
    } catch (error) {
      print("Error updating restaurant $id: $error");
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<void> deleteRestaurant(int id) async {
    _isLoading = true;
    notifyListeners();
    try {
      await RestaurantService().deleteRestaurant(id);
      _restaurants.removeWhere((r) => r.id == id);
    } catch(error)
    {
      print("Error deleting restaurant $id: $error");
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  void setSelectedRestaurant(Restaurant restaurant) {
    _selectedRestaurant = restaurant;
    notifyListeners();
  }

  void initWithoutBackEnd() {
    _restaurants = [
      Restaurant(id: 1, name: "Shaormeria 1", addressId: 1),
      Restaurant(id: 2, name: "Shaormeria 2", addressId: 2)
    ];
    notifyListeners();
  }

}
