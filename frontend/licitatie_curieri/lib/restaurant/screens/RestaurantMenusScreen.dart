import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../common/widgets/CartActionBarButton.dart';
import '../../common/widgets/ListItemCustomCard.dart';
import '../providers/MenuItemProvider.dart';
import '../providers/RestaurantProvider.dart';
import '../services/CartService.dart';

class RestaurantMenusScreen extends StatefulWidget {
  const RestaurantMenusScreen({Key? key}) : super(key: key);

  @override
  State<RestaurantMenusScreen> createState() => _RestaurantMenusScreenState();
}

class _RestaurantMenusScreenState extends State<RestaurantMenusScreen> {

  GlobalKey<CartActionBarButtonState> _cartKey = GlobalKey();

  CartService cartService = CartService();


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

        actions: [
          CartActionBarButton(key: _cartKey, canRedirect: true),
          SizedBox(width: 20.0),
        ],
      ),
      body: menuProvider.isLoading
          ? const Center(child: CircularProgressIndicator())
          : menuProvider.menuItems.isEmpty
          ? const Center(child: Text("No menus found."))
          : ListView.builder(
        itemCount: menuProvider.menuItems.length,
        itemBuilder: (context, i) {
          final menuItem = menuProvider.menuItems[i];
          return ListItemCustomCard.fromMenuItem(
            menuItem,
             "Add to cart",
            () => (_cartKey.currentState)!.addToCart(menuItem, cartService, restaurantProvider.selectedRestaurant!.id),
          );
        },
      ),
    );
  }
}
