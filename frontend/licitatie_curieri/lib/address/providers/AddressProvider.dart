import 'package:flutter/material.dart';
import 'package:licitatie_curieri/address/models/AddressModel.dart';
import 'package:licitatie_curieri/address/services/AddressService.dart';


class AddressProvider with ChangeNotifier {
  List<Address> _addresses = [];
  Address? _selectedAddress;
  bool _isLoading = false;

  List<Address> get addresses => _addresses;
  Address? get selectedAddress => _selectedAddress;
  bool get isLoading => _isLoading;

  Future<void> fetchAddresses() async {
    _isLoading = true;
    notifyListeners();

    try {
      _addresses = await AddressService().fetchAddresses();

    } catch (error) {
      print("Error fetching addresses: $error");
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }


  Future<void> fetchAddressesByUserId(int userId) async {
    _isLoading = true;
    notifyListeners();


    print("lista adrese provider: " + addresses.length.toString());
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
      _addresses.add(address); // Adaugă adresa în listă
      notifyListeners();
      return true; // Succes
    } catch (e) {
      return false; // Eșec
    }
  }


  void setSelectedAddress(Address address) {
    _selectedAddress = address;
    notifyListeners();
  }


}
