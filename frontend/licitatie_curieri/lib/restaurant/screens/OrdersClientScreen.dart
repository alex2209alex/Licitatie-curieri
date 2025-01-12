import 'package:flutter/material.dart';
import 'package:licitatie_curieri/address/screens/AddressScreen.dart';
import 'package:licitatie_curieri/restaurant/providers/OrderProvider.dart';
import 'package:provider/provider.dart';

import '../../common/widgets/CartActionBarButton.dart';
import '../../common/widgets/ListItemCustomCard.dart';

class OrdersClientScreen extends StatefulWidget {
  const OrdersClientScreen({Key? key}) : super(key: key);

  @override
  State<OrdersClientScreen> createState() => _OrdersClientScreenState();
}

class _OrdersClientScreenState extends State<OrdersClientScreen> {
  @override
  void initState() {
    super.initState();

    WidgetsBinding.instance.addPostFrameCallback((_) {
      initData();
    });
  }

  Future<void> initData() async {
    final orderProvider =
        Provider.of<OrderProvider>(context, listen: false);
    await orderProvider.fetchOrdersClient();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Orders'),
        centerTitle: true,
        actions: [
          CartActionBarButton(canRedirect: true),
          SizedBox(width: 20.0),
        ],
      ),
      body: Consumer<OrderProvider>(
              builder: (context, orderProvider, _) {
                if (orderProvider.isLoading) {
                  return const Center(
                    child: CircularProgressIndicator(),
                  );
                }

                if (orderProvider.orders.isEmpty) {
                  return const Center(
                    child: Text("No orders found."),
                  );
                }

                return ListView.builder(
                  itemCount: orderProvider.orders.length,
                  itemBuilder: (context, i) {
                    final order = orderProvider.orders[i];
                    return ListItemCustomCard.fromOrder(
                      order,
                      "View",
                      () {
                        orderProvider.setSelectedOrder(order);
                        Navigator.push(
                          context,
                          MaterialPageRoute(
                            builder: (context) => const OrdersClientScreen(),   // To do: To-Do: W.I.P.: WIP: Change to detailed order screen
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
          Navigator.push(
            context,
            MaterialPageRoute(
              builder: (context) => const AddressScreen(),
            ),
          ).then((_) {
            initData();
          });
        },
        child: const Icon(Icons.location_on),
        tooltip: 'Go to Addresses',
      ),
    );
  }
}
