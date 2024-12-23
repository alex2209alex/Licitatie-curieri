import 'dart:developer';

import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:licitatie_curieri/address/models/AddressModel.dart';
import 'package:licitatie_curieri/address/services/AddressService.dart';

class AddressProvider with ChangeNotifier {
  List<Address> _addresses = [];
  Address? _selectedAddress;
  bool _isLoading = false;
  int? _selectedAddressId;

  List<Address> get addresses => _addresses;
  Address? get selectedAddress => _selectedAddress;
  bool get isLoading => _isLoading;
  int? get selectedAddressId => _selectedAddressId;



  Future<void> fetchAddresses() async {
    _isLoading = true;
    notifyListeners();

    try {
      _addresses = await AddressService().fetchAddresses();
      await _loadSelectedAddress();  // Try to load the last selected address
    } catch (error) {
      print("Error fetching addresses: $error");
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }


  Future<Address?> fetchAddressById(int addressId) async
  {
    _isLoading = true;
    notifyListeners();

    await fetchAddresses();

    for(Address address in _addresses)
      {
        if(address.id == addressId)
        {
          return address;
        }
      }
    return null;
  }

  Future<Address?> fetchAddressFromCoordinates(double latitude, double longitude) async
  {
    _isLoading = true;
    notifyListeners();

    await fetchAddresses();

    for(Address address in _addresses)
      {
        if(address.latitude == latitude && address.longitude == longitude)
          {
            return address;
          }
      }
    return null;
  }



  Future<void> _loadSelectedAddress() async {
    final prefs = await SharedPreferences.getInstance();
    final selectedAddressId = prefs.getInt('selected_address_id');
    _selectedAddressId = selectedAddressId;
    notifyListeners();
  }

  Future<void> saveSelectedAddress(Address address) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setInt('selected_address_id', address.id);
    _selectedAddress = address;
    _selectedAddressId = address.id;
    notifyListeners();
  }

  Future<void> fetchAddressesByUserId(int userId) async {
    _isLoading = true;
    notifyListeners();

    try {
      _addresses = await AddressService().fetchAddressesByUserId(userId);
    } catch (e) {
      print("Error fetching addresses for user $userId: $e");
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<bool> createAddress(Address address) async {
    try {
      final newAddress = await AddressService().createAddress(address);
      _addresses.add(newAddress);
      notifyListeners();
      return true;
    } catch (e) {
      return false;
    }
  }

  void setSelectedAddress(Address address) {
    _selectedAddress = address;
    _selectedAddressId = address.id;
    saveSelectedAddress(address);
    notifyListeners();
  }




}
