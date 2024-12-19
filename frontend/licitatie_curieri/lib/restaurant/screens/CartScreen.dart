import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../common/widgets/CartActionBarButton.dart';
import '../../common/widgets/ListItemCustomCard.dart';
import '../models/MenuItemModel.dart';
import '../providers/MenuItemProvider.dart';
import '../providers/RestaurantProvider.dart';
import '../services/CartService.dart';

class CartScreen extends StatefulWidget {
  const CartScreen({Key? key}) : super(key: key);

  @override
  State<CartScreen> createState() => _CartScreenState();
}

class _CartScreenState extends State<CartScreen> {
  GlobalKey<CartActionBarButtonState> _cartKey = GlobalKey();

  CartService cartService = CartService();

  @override
  Widget build(BuildContext context) {
    Future<List<MenuItem>> cartItems = cartService.getCartItems();

    return Scaffold(
      appBar: AppBar(
        title: const Text("My Cart"),
        centerTitle: true,
        actions: [
          CartActionBarButton(key: _cartKey, canRedirect: false),
          const SizedBox(width: 20.0),
        ],
      ),
      body: FutureBuilder<List<MenuItem>>(
        future: cartItems,
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          }
          else if (!snapshot.hasData || snapshot.data!.isEmpty) {
            return const Center(child: Text("There are no items in your cart"));
          }

          final items = snapshot.data!;
          return ListView.builder(
            itemCount: items.length,
            itemBuilder: (context, i) {
              final menuItem = items[i];
              return ListItemCustomCard.fromMenuItem(
                menuItem,
                "Remove",
                () {
                  (_cartKey.currentState)!.removeFromCart(i, cartService);

                  //  Delete this widget from the cart screen
                  setState(() {
                    items.removeAt(i);
                  });
                }
              );
            },
          );
        },
      ),
    );
  }
}

