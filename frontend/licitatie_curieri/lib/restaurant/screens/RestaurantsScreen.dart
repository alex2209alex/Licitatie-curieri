import 'package:flutter/material.dart';
import 'package:licitatie_curieri/address/screens/AddressScreen.dart';
import 'package:licitatie_curieri/restaurant/providers/RestaurantProvider.dart';
import 'package:provider/provider.dart';
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


    WidgetsBinding.instance.addPostFrameCallback((_)
    {
      final restaurantProvider = Provider.of<RestaurantProvider>(context, listen: false);
      restaurantProvider.fetchRestaurants();
    });

  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Restaurants'),
        centerTitle: true,
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
        onPressed: () {
          Navigator.push(context,
          MaterialPageRoute(
            builder: (context) => const AddressScreen(),
            ),
          );
        },
        child: const Icon(Icons.location_on),
        tooltip: 'Go to Addresses',
      ),
    );
  }
}
