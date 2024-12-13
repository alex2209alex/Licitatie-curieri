import 'package:flutter/cupertino.dart';
import 'package:licitatie_curieri/restaurant/services/RestaurantService.dart';

import '../models/RestaurantModel.dart';

class RestaurantProvider with ChangeNotifier {

  List<Restaurant> _restaurants = [];
  Restaurant? _selectedRestaurant;
  bool _isLoading = false;

  List<Restaurant> get restaurants => _restaurants;
  Restaurant? get selectedRestaurant => _selectedRestaurant;
  bool get isLoading => _isLoading;

  set selectedRestaurant(Restaurant? restaurant)
  {
    _selectedRestaurant = restaurant;
  }

  Future<void> fetchRestaurants() async {
    _isLoading = true;
    notifyListeners();
    await Future.delayed(Duration(seconds: 4));
    try{
      _restaurants = await RestaurantService().fetchRestaurants();
    }catch(error)
    {
      print("Error fetching restaurants: $error");
    } finally{

      initWithoutBackEnd();
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<void> fetchRestaurantById(int id) async {
    _isLoading = true;
    notifyListeners();

    try {
      _selectedRestaurant = await RestaurantService().fetchRestaurantById(id);
    } catch(error)
    {
      print("Error fetching restaurant $id: $error");
    } finally {
      _isLoading = false;
      notifyListeners();
    }

  }

  void setSelectedRestaurant(Restaurant restaurant) {
    _selectedRestaurant = restaurant;
    notifyListeners();
  }

  void initWithoutBackEnd()
  {
    _restaurants = [
      Restaurant(id: 1, name: "shaormeria 1", addressId: 1),
      Restaurant(id: 2, name: "shaormeria 2", addressId: 2)
    ];
  }

}