import 'package:flutter/material.dart';
import 'package:licitatie_curieri/restaurant/models/RestaurantMenuItemModel.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../../restaurant/models/MenuItemModel.dart';
import '../../restaurant/providers/CartProvider.dart';
import '../../restaurant/screens/CartScreen.dart';
import '../../restaurant/services/CartService.dart';
import 'package:provider/provider.dart';


class CartActionBarButton extends StatefulWidget {
  final bool canRedirect;

  CartActionBarButton({
    Key? key,
    required this.canRedirect,
  }) : super(key: key);

  @override
  State<CartActionBarButton> createState() => CartActionBarButtonState();
}

class CartActionBarButtonState extends State<CartActionBarButton> {
  CartProvider? cartProvider;
  int cartCounter = 0;

  @override
  void initState() {
    super.initState();
    _loadCartCounter();
  }

  Future<void> _loadCartCounter() async {
    final prefs = await SharedPreferences.getInstance();

    //prefs.setInt('cartCounter', 0);
    setState(() {
      cartCounter = prefs.getInt('cartCounter') ?? 0;
      cartProvider!.setCounter(cartCounter);
    });
  }

  Future<void> addToCart(MenuItem menuItem, CartService cartService, int restaurantId) async {

    RestaurantMenuItem restaurantMenuItem = RestaurantMenuItem(restaurantId: restaurantId, menuItem: menuItem);

    //  Add the restaurant menu item to the cart list
    int count = await cartService.addItemToCart(restaurantMenuItem);

    setState(() {
      cartProvider!.setCounter(count);
    });
  }

  Future<void> removeFromCart(int index, CartService cartService) async {
    //  Remove the item corresponding to this index from the cart list
    int count = await cartService.removeItemAtIndex(index);

    setState(() {
      cartProvider!.setCounter(count);
    });
  }

  @override
  Widget build(BuildContext context) {

    cartProvider = Provider.of<CartProvider>(context);

    return
      Padding(
        padding: const EdgeInsets.all(12.0),
        child: Stack(
          clipBehavior: Clip.none,
          children: [
            FloatingActionButton(
                heroTag: UniqueKey(),
                onPressed: () {
                  if (widget.canRedirect) {
                    Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (context) => const CartScreen(),
                      ),
                    );
                  }
                },

                child: const Icon(Icons.shopping_cart)),
                        if (cartProvider!.cartCounter > 0)
                          Positioned(
                            right: -4,
                            top: -8,
                            child: Container(
                              padding: const EdgeInsets.only(left: 4.0, right: 4.0),
                              decoration: const BoxDecoration(
                                color: Colors.red,
                                shape: BoxShape.circle,
                              ),
                              child: Text(
                                '${cartProvider!.cartCounter}',
                                style: const TextStyle(
                                  color: Colors.white,
                                  fontSize: 12.0,
                                  fontWeight: FontWeight.bold,
                                ),
                              ),
                            ),
                          ),
          ],
        ),
      );
  }
}