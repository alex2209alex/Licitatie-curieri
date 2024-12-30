import 'dart:developer';

import 'package:flutter/material.dart';
import 'package:licitatie_curieri/address/providers/AddressProvider.dart';
import 'package:provider/provider.dart';
import '../../address/models/AddressModel.dart';
import '../../common/Utils.dart';
import '../models/RestaurantModel.dart';
import '../providers/RestaurantProvider.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

import 'MenusManageScreen.dart';

class RestaurantsManageScreen extends StatefulWidget {
  const RestaurantsManageScreen({Key? key}) : super(key: key);

  @override
  State<RestaurantsManageScreen> createState() => _RestaurantsManageScreenState();
}

class _RestaurantsManageScreenState extends State<RestaurantsManageScreen> {
  final _formKey = GlobalKey<FormState>();
  String? _name;
  double? _latitude;
  double? _longitude;

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      Provider.of<RestaurantProvider>(context, listen: false).fetchRestaurants();
    });
  }

  void _showAddEditDialog(BuildContext context, {Restaurant? restaurant}) {

    final TextEditingController _addressController = TextEditingController();
    final TextEditingController _latitudeController = TextEditingController();
    final TextEditingController _longitudeController = TextEditingController();
    bool isEditing = false;
    bool isValidAddress = false;

    if (restaurant != null) {
      _name = restaurant.name;
      _addressController.text = "";
      _latitudeController.text = "";
      _longitudeController.text = "";
      isValidAddress = true;
    } else {
      _name = null;
      _addressController.clear();
      _latitudeController.clear();
      _longitudeController.clear();
      isValidAddress = false;
    }

    Future<void> _fetchCoordinatesForAddress() async {
      isValidAddress = false;

      final address = _addressController.text;
      if (address.isEmpty) return;

      final apiKey = Utils.mapsApiKey;
      final url =
          "https://maps.googleapis.com/maps/api/geocode/json?address=${Uri.encodeComponent(address)}&key=$apiKey";

      try {
        final response = await http.get(Uri.parse(url));
        final data = json.decode(response.body);

        if (data['status'] == 'OK') {
          final location = data['results'][0]['geometry']['location'];
          final latitude = location['lat'];
          final longitude = location['lng'];

          _latitudeController.text = latitude.toString();
          _longitudeController.text = longitude.toString();
          isValidAddress = true;
        } else {
          isValidAddress = false;
          _showErrorDialog(context, "Invalid address. Please try again.");
        }
      } catch (e) {
        isValidAddress = false;
        _showErrorDialog(context, "An error occurred. Please try again.");
      }
    }

    showDialog(
      context: context,
      builder: (ctx) => StatefulBuilder(
        builder: (ctx, setState) => AlertDialog(
          title: Text(restaurant == null ? "Add Restaurant" : "Edit Restaurant"),
          content: Form(
            key: _formKey,
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                TextFormField(
                  initialValue: _name,
                  decoration: const InputDecoration(labelText: "Name"),
                  validator: (value) {
                    if (value == null || value.isEmpty) {
                      return "Please enter a name.";
                    }
                    return null;
                  },
                  onSaved: (value) => _name = value,
                ),
                TextField(
                  controller: _addressController,
                  decoration: const InputDecoration(labelText: "Address"),
                  onTap: () => setState(() => isEditing = true),
                  onChanged: (_) => setState(() => isEditing = true),
                  onEditingComplete: () async {
                    setState(() => isEditing = false);
                    await _fetchCoordinatesForAddress();
                    setState(() {});
                  },
                ),
                TextField(
                  controller: _latitudeController,
                  decoration: const InputDecoration(labelText: "Latitude"),
                  readOnly: true,
                ),
                TextField(
                  controller: _longitudeController,
                  decoration: const InputDecoration(labelText: "Longitude"),
                  readOnly: true,
                ),
              ],
            ),
          ),
          actions: [
            TextButton(
              onPressed: () => Navigator.of(ctx).pop(),
              child: const Text("Cancel"),
            ),
            ElevatedButton(
              onPressed: (isEditing || !isValidAddress)
                  ? null
                  : () {
                if (_formKey.currentState!.validate()) {
                  _formKey.currentState!.save();
                  if (restaurant == null) {
                    _addRestaurant(
                      context,
                      _addressController.text,
                      double.parse(_latitudeController.text),
                      double.parse(_longitudeController.text),
                    );
                  } else {
                    _updateRestaurant(
                      context,
                      restaurant.id,
                      _addressController.text,
                      double.parse(_latitudeController.text),
                      double.parse(_longitudeController.text),
                    );
                  }
                  Navigator.of(ctx).pop();
                }
              },
              child: Text(restaurant == null ? "Add" : "Update"),
            ),
          ],
        ),
      ),
    );
  }

  Future<void> _addRestaurant(
      BuildContext context,
      String address,
      double latitude,
      double longitude,
      ) async {
    final provider = Provider.of<RestaurantProvider>(context, listen: false);
    final newRestaurant = Restaurant(
      id: 0, // DOESN'T MATTER
      name: _name!,
      addressId: 0, // DOESN'T MATTER
    );
    final addressProvider = Provider.of<AddressProvider>(context, listen: false);
    final response = await provider.createRestaurant(newRestaurant, Address(id: 0,details: address,latitude: latitude,longitude: longitude), addressProvider);
    final responseJson = json.decode(response.body);
    if (responseJson.containsKey('id')) {
      final createdRestaurantId = responseJson['id'];
      newRestaurant.id = createdRestaurantId;
      log('Restaurant created with ID: $createdRestaurantId');
    } else {
      _showErrorDialog(context, 'Failed to create restaurant.');
    }
  }

  Future<void> _updateRestaurant(
      BuildContext context,
      int id,
      String address,
      double latitude,
      double longitude,
      ) async {
    final provider = Provider.of<RestaurantProvider>(context, listen: false);
    final updatedRestaurant = Restaurant(
      id: id,
      name: _name!,
      addressId: 0, // Adjust as needed
    );
    final addressProvider = Provider.of<AddressProvider>(context, listen: false);

    await provider.updateRestaurant(id, updatedRestaurant);
  }

  void _showErrorDialog(BuildContext context, String message) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: const Text("Error"),
          content: Text(message),
          actions: [
            TextButton(
              onPressed: () {
                Navigator.of(context).pop();
              },
              child: const Text("OK"),
            ),
          ],
        );
      },
    );
  }


  Future<void> _deleteRestaurant(BuildContext context, int id) async {
    final provider = Provider.of<RestaurantProvider>(context, listen: false);
    await provider.deleteRestaurant(id);
    setState(() {

    });
  }

  @override
  Widget build(BuildContext context) {
    final restaurantProvider = Provider.of<RestaurantProvider>(context);

    return Scaffold(
      appBar: AppBar(
        title: const Text("Manage Restaurants"),
        centerTitle: true,
      ),
      body: restaurantProvider.isLoading
          ? const Center(child: CircularProgressIndicator())
          : ListView.builder(
        itemCount: restaurantProvider.restaurants.length,
        itemBuilder: (ctx, index) {
          final restaurant = restaurantProvider.restaurants[index];
          return ListTile(
            title: Text(restaurant.name),
            subtitle: Text("${restaurant.address?.details ?? "Address Not Found" }"),
            trailing: Row(
              mainAxisSize: MainAxisSize.min,
              children: [
                IconButton(
                  icon: const Icon(Icons.edit),
                  onPressed: () => _showAddEditDialog(context, restaurant: restaurant),
                ),
                IconButton(
                  icon: const Icon(Icons.delete),
                  onPressed: () => _deleteRestaurant(context, restaurant.id),
                ),

                Padding(
                    padding: const EdgeInsets.all(12.0),
                    child:
                    FloatingActionButton(
                        heroTag: UniqueKey(),
                        onPressed: () {
                          Navigator.push(
                            context,
                            MaterialPageRoute(
                              builder: (context) => MenusManageScreen(restaurantId: restaurant.id),
                            ),
                          );
                        },
                        child: const Icon(Icons.restaurant_menu))
                )
              ],
            ),
          );
        },
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () => _showAddEditDialog(context),
        child: const Icon(Icons.add),
      ),
    );
  }
}
