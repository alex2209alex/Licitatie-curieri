import 'dart:async';
import 'dart:developer';
import 'package:flutter/material.dart';
import 'package:licitatie_curieri/restaurant/providers/OrderProvider.dart';
import 'package:provider/provider.dart';

import '../../common/widgets/CartActionBarButton.dart';
import '../../common/widgets/ListItemCustomCard.dart';
import '../models/OrderModel.dart';
import 'OrderDetailsScreen.dart';

class OrdersCourierScreen extends StatefulWidget {
  const OrdersCourierScreen({Key? key}) : super(key: key);

  @override
  State<OrdersCourierScreen> createState() => _OrdersCourierScreenState();
}

class _OrdersCourierScreenState extends State<OrdersCourierScreen> {
  late Timer _timer;

  @override
  void initState() {
    super.initState();

    WidgetsBinding.instance.addPostFrameCallback((_) {
      initData();
    });

    _timer = Timer.periodic(const Duration(seconds: 20), (_) {
      log("Refreshing Orders");
      initData();
    });
  }

  Future<void> initData() async {
    final orderProvider = Provider.of<OrderProvider>(context, listen: false);
    await orderProvider.fetchOrdersCourier(); // Fetch orders for couriers
  }

  @override
  void dispose() {
    // Cancel the timer when the screen is disposed
    _timer.cancel();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Courier Orders'),
        centerTitle: true,
        actions: [
          CartActionBarButton(canRedirect: true),
          const SizedBox(width: 20.0),
        ],
      ),
      body: Consumer<OrderProvider>(
        builder: (context, orderProvider, _) {
          if (orderProvider.isLoading) {
            return const Center(child: CircularProgressIndicator());
          }

          if (orderProvider.orders.isEmpty) {
            return const Center(child: Text("No orders found."));
          }

          return ListView.builder(
            itemCount: orderProvider.orders.length,
            itemBuilder: (context, i) {
              final order = orderProvider.orders[i];

              // Cast to OrderDetails to access extended properties
              if (order is OrderDetails) {
                return ListTile(
                  title: Text(order.restaurantAddress),
                  subtitle: Text("Status: ${order.orderStatus}.\nOrder of ${order.foodPrice} RON"),
                  trailing: Row(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      IconButton(
                        icon: const Icon(Icons.open_in_new),
                        onPressed: () {
                          // Navigate to OrderDetailsScreen
                          Navigator.push(
                            context,
                            MaterialPageRoute(
                              builder: (context) => OrderDetailsScreen(orderId: order.id),
                            ),
                          );
                        },
                      ),
                      if (order.orderStatus != "CANCELLED")
                        IconButton(
                          icon: const Icon(Icons.cancel),
                          onPressed: () {
                            orderProvider.cancelOrder(order.id);
                          },
                        ),
                    ],
                  ),
                );
              } else {
                return const SizedBox(); // Handle case if it's not OrderDetails
              }
            },
          );
        },
      ),
    );
  }
}
