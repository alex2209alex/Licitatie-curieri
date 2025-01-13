import 'dart:convert';
import 'dart:developer';

import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:licitatie_curieri/address/providers/AddressProvider.dart';
import 'package:licitatie_curieri/common/GetToken.dart';
import 'package:licitatie_curieri/restaurant/screens/OrdersClientScreen.dart';
import 'package:provider/provider.dart';

import '../../common/Utils.dart';
import '../../common/widgets/CartActionBarButton.dart';
import '../../common/widgets/ListItemCustomCard.dart';
import '../../common/widgets/LogoutActionBarButton.dart';
import '../models/RestaurantMenuItemModel.dart';
import '../providers/CartProvider.dart';
import '../services/CartService.dart';

class CartScreen extends StatefulWidget {
  const CartScreen({Key? key}) : super(key: key);

  @override
  State<CartScreen> createState() => _CartScreenState();
}

class _CartScreenState extends State<CartScreen> {
  final CartService cartService = CartService();

  Future<void> _placeOrder(List<RestaurantMenuItem> restaurantMenuItems) async {
    const String apiUrl = '${Utils.BASE_URL}/orders';

    final int addressId =
        Provider.of<AddressProvider>(context, listen: false).selectedAddressId!;
    final double deliveryPriceLimit = 14.43;

    // Prepare items for the request body
    final List<Map<String, dynamic>> items = restaurantMenuItems.map((rmi) {
      log("rmi id ${rmi.menuItem.id}");
      return {
        "id": rmi.menuItem.id,
        "quantity": 1,
      };
    }).toList();

    if (items.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text("Your cart is empty!")),
      );
      return;
    }

    final Map<String, dynamic> requestBody = {
      "addressId": addressId,
      "deliveryPriceLimit": deliveryPriceLimit,
      "items": items,
    };
    log("deliveryPriceLimit: ${deliveryPriceLimit.toString()}");

    String? token = await GetToken().getToken();

    try {
      final response = await http.post(
        Uri.parse(apiUrl),
        headers: {
          "Content-Type": "application/json",
          'Authorization': 'Bearer $token'
        },
        body: jsonEncode(requestBody),
      );

      if (response.statusCode == 201) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text("Order placed successfully!")),
        );

        // Clear the cart
        await cartService.saveCartItems([]);
        context.read<CartProvider>().setCounter(0);
        setState(() {});
      } else {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text("Failed to place order: ${response.body}"),
          ),
        );
      }
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text("An error occurred: $e")),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("My Cart"),
        centerTitle: true,
        actions: [
          Offstage(
              offstage: true,
              child: CartActionBarButton(
                key: GlobalKey<CartActionBarButtonState>(),
                canRedirect: false,
              ),
          ),

          LogoutActionBarButton(),
          const SizedBox(width: 20.0),

          Padding(
            padding: const EdgeInsets.only(right: 12.0, top: 12.0, bottom: 12.0, left: 0.0),
            child: Stack(
            clipBehavior: Clip.none,
            children: [
              FloatingActionButton(
                  heroTag: UniqueKey(),
                  onPressed: () {
                    Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (context) => const OrdersClientScreen(),
                      ),
                    );
                  },

                  child: const Icon(Icons.fastfood_outlined))
            ])),
          SizedBox(width: 20.0),
        ],
      ),
      body: Consumer<CartProvider>(
        builder: (context, cartProvider, child) {
          return FutureBuilder<List<RestaurantMenuItem>>(
            future: cartService.getCartItems(),
            builder: (context, snapshot) {
              if (snapshot.connectionState == ConnectionState.waiting) {
                return const Center(child: CircularProgressIndicator());
              } else if (!snapshot.hasData || snapshot.data!.isEmpty) {
                return const Center(
                    child: Text("There are no items in your cart"));
              }

              final restaurantMenuItems = snapshot.data!;
              final items =
                  restaurantMenuItems.map((rmi) => rmi.menuItem).toList();

              return Column(
                children: [
                  Expanded(
                    child: ListView.builder(
                      itemCount: items.length,
                      itemBuilder: (context, i) {
                        final menuItem = items[i];
                        return ListItemCustomCard.fromMenuItem(
                          menuItem,
                          "Remove",
                          () async {
                            await cartService.removeItemAtIndex(i);
                            cartProvider
                                .setCounter(restaurantMenuItems.length - 1);
                            setState(() {});
                          },
                        );
                      },
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.all(20.0),
                    child: FloatingActionButton.extended(
                      heroTag: "orderButton",
                      onPressed: () => _placeOrder(restaurantMenuItems),
                      icon: const Icon(Icons.shopping_cart_checkout_sharp),
                      label: const Text("Place order"),
                    ),
                  ),
                ],
              );
            },
          );
        },
      ),
    );
  }
}
