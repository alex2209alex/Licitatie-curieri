import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../common/widgets/ListItemCustomCard.dart';
import '../providers/MenuItemProvider.dart';
import '../providers/RestaurantProvider.dart';

class RestaurantMenusScreen extends StatefulWidget {
  const RestaurantMenusScreen({Key? key}) : super(key: key);

  @override
  State<RestaurantMenusScreen> createState() => _RestaurantMenusScreenState();
}

class _RestaurantMenusScreenState extends State<RestaurantMenusScreen> {
  @override
  void initState() {
    super.initState();


    WidgetsBinding.instance.addPostFrameCallback((_)
    {
      final restaurantProvider =
      Provider.of<RestaurantProvider>(context, listen: false);
      final menuProvider = Provider.of<MenuItemProvider>(context, listen: false);

      if (restaurantProvider.selectedRestaurant != null) {
        menuProvider.fetchMenuItems(restaurantProvider.selectedRestaurant!.id);
      }
    }
    );

  }

  @override
  Widget build(BuildContext context) {
    final restaurantProvider = Provider.of<RestaurantProvider>(context);
    final menuProvider = Provider.of<MenuItemProvider>(context);

    return Scaffold(
      appBar: AppBar(
        title: Text(restaurantProvider.selectedRestaurant!.name),
        centerTitle: true,
      ),
      body: menuProvider.isLoading
          ? const Center(child: CircularProgressIndicator())
          : menuProvider.MenuItems.isEmpty
          ? const Center(child: Text("No menus found."))
          : ListView.builder(
        itemCount: menuProvider.MenuItems.length,
        itemBuilder: (context, i) {
          final menuItem = menuProvider.MenuItems[i];
          return ListItemCustomCard.fromMenuItem(
            menuItem,
             "Add to cart",
                () {
              // Add to cart function
            },
          );
        },
      ),
    );
  }
}
