import 'dart:convert';

import 'package:flutter/material.dart';
import '../../common/widgets/CartActionBarButton.dart';
import '../../common/widgets/ListItemCustomCard.dart';
import '../models/MenuItemModel.dart';
import '../models/RestaurantMenuItemModel.dart';
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
    Future<List<RestaurantMenuItem>> cartItems = cartService.getCartItems();
    List<MenuItem> items = [];
    List<RestaurantMenuItem> restaurantMenuItems = [];

    return Scaffold(
      appBar: AppBar(
        title: const Text("My Cart"),
        centerTitle: true,
        actions: [
          Offstage(
              offstage: true,
              child: CartActionBarButton(key: _cartKey, canRedirect: false)
          ),
          const SizedBox(width: 20.0),
        ],
      ),
      body: Column(
        children: [
          FutureBuilder<List<RestaurantMenuItem>>(
            future: cartItems,
            builder: (context, snapshot) {
              if (snapshot.connectionState == ConnectionState.waiting) {
                return const Center(child: CircularProgressIndicator());
              }
              else if (!snapshot.hasData || snapshot.data!.isEmpty) {
                return const Center(child: Text("There are no items in your cart"));
              }

              restaurantMenuItems = snapshot.data!;
              items = restaurantMenuItems.map((rmi) => rmi.menuItem).toList();

              return Expanded(
                child: ListView.builder(
                  shrinkWrap: true,
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
                ),
              );
            },
          ),
        ],
      ),

      bottomNavigationBar: Padding(
        padding: const EdgeInsets.only(left: 50.0, right: 50.0, bottom: 20.0, top: 20.0),
        child: FloatingActionButton.extended(
          heroTag: "orderButton",
          onPressed: () {
            //  Debug info
            for (RestaurantMenuItem rmi in restaurantMenuItems) {
              print("-----\nRestaurant ID: ${rmi.restaurantId}");
              print("MenuItem ID: ${rmi.menuItem.id}, name ${rmi.menuItem.name}");
            }

            //  Return if cart is empty
            List<List<int>> sentData = restaurantMenuItems.map((rmi) => [rmi.restaurantId, rmi.menuItem.id]).toList();
            if (sentData.isEmpty) {
              return;
            }

            //  Show popup if items are from >1 restaurants
            bool moreThanOneRestaurant = false;
            int uniqueRestaurantId = sentData[0][0];
            for (List<int> menuRestaurantPair in sentData) {
              if (menuRestaurantPair[0] != uniqueRestaurantId) {
                moreThanOneRestaurant = true;
                break;
              }
            }

            if (moreThanOneRestaurant) {
              showDialog(
                context: context,
                builder: (BuildContext context) {
                  return const AlertDialog(
                    content: Text("You cannot order items from more than one restaurant!"),
                  );
                },
              );
            }
            else {
              //  Send final JSON to backend (W.I.P.)
              print("\n\nFinal JSON: ");
              print(jsonEncode(sentData));
            }
          },

          icon: const Row(
            children: [
              Icon(Icons.shopping_cart_checkout_sharp),
            ],
          ),
          label: const Text(
              "Place order",
              overflow: TextOverflow.ellipsis
          ),
        ),
      ),
    );
  }
}

