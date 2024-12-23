import 'dart:developer';

import 'package:flutter/material.dart';
import 'package:licitatie_curieri/address/providers/AddressProvider.dart';
import 'package:licitatie_curieri/address/screens/AddressScreen.dart';
import 'package:licitatie_curieri/restaurant/providers/RestaurantProvider.dart';
import 'package:provider/provider.dart';
import '../../common/widgets/CartActionBarButton.dart';
import '../../common/widgets/ListItemCustomCard.dart';
import 'RestaurantMenusScreen.dart';


class RestaurantsScreen extends StatefulWidget {
  const RestaurantsScreen({Key? key}) : super(key: key);

  @override
  State<RestaurantsScreen> createState() => _RestaurantsScreenState();
}

class _RestaurantsScreenState extends State<RestaurantsScreen> {
  @override
  void initState() {
    super.initState();


    WidgetsBinding.instance.addPostFrameCallback((_){
      initData();
    });

  }

  Future<void> initData() async
  {
    final restaurantProvider = Provider.of<RestaurantProvider>(context, listen: false);
    final addressProvider = Provider.of<AddressProvider>(context, listen: false);
    await addressProvider.fetchAddresses();
    if(addressProvider.selectedAddressId != null)
      {
        final selectedAddressId = addressProvider.selectedAddressId;
        await restaurantProvider.fetchRestaurantsByUserIdForClientOnly(selectedAddressId!, addressProvider);
      }

  }
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Restaurants'),
        centerTitle: true,

        actions: [
          CartActionBarButton(canRedirect: true),
          SizedBox(width: 20.0),
        ],
      ),
      body: Consumer<RestaurantProvider>(
        builder: (context, restaurantProvider, _) {
          if (restaurantProvider.isLoading) {
            return const Center(
              child: CircularProgressIndicator(),
            );
          }

          if (restaurantProvider.restaurants.isEmpty) {
            return const Center(
              child: Text("No restaurants found."),
            );
          }

          return ListView.builder(
            itemCount: restaurantProvider.restaurants.length,
            itemBuilder: (context, i) {
              final restaurant = restaurantProvider.restaurants[i];
              return ListItemCustomCard.fromRestaurant(
                restaurant,
                "View",
                    () {
                    restaurantProvider.setSelectedRestaurant(restaurant);
                    Navigator.push(
                    context,
                    MaterialPageRoute(
                      builder: (context) => const RestaurantMenusScreen(),
                    ),
                  );
                },
              );
            },
          );
        },
      ),
      floatingActionButton: FloatingActionButton(
        heroTag: UniqueKey(),
        onPressed: () {
          Navigator.push(context,
          MaterialPageRoute(
            builder: (context) => const AddressScreen(),
            ),
          ).then((_)
          {
            initData();
          });
        },
        child: const Icon(Icons.location_on),
        tooltip: 'Go to Addresses',
      ),
    );
  }
}
