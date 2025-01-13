import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:licitatie_curieri/restaurant/providers/OrderProvider.dart';
import '../models/OrderModel.dart';

class OrderDetailsScreen extends StatelessWidget {
  final int orderId;

  const OrderDetailsScreen({Key? key, required this.orderId}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Order Details'),
        centerTitle: true,
      ),
      body: FutureBuilder<OrderToDeliverDetails?>(
        future: Provider.of<OrderProvider>(context, listen: false)
            .fetchOrderDetails(orderId),
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          }

          if (snapshot.hasError) {
            return Center(child: Text("Error: ${snapshot.error}"));
          }

          if (!snapshot.hasData) {
            return const Center(child: Text("Order details not found."));
          }

          final order = snapshot.data!;

          return Padding(
            padding: const EdgeInsets.all(16.0),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  'Delivery Address: ${order.deliveryAddress}',
                  style: Theme.of(context).textTheme.titleLarge,
                ),
                const SizedBox(height: 8.0),
                Text(
                  'Food Price: ${order.foodPrice.toStringAsFixed(2)} RON',
                  style: Theme.of(context).textTheme.bodyLarge,
                ),
                const SizedBox(height: 8.0),
                Text(
                  'Delivery Price: ${order.deliveryPrice.toStringAsFixed(2)} RON',
                  style: Theme.of(context).textTheme.bodyLarge,
                ),
                const SizedBox(height: 16.0),
                Text(
                  'Items:',
                  style: Theme.of(context).textTheme.titleMedium,
                ),
                const SizedBox(height: 8.0),
                Expanded(
                  child: ListView.builder(
                    itemCount: order.items.length,
                    itemBuilder: (context, index) {
                      final item = order.items[index];
                      return ListTile(
                        title: Text(item.name),
                        trailing: Text('Quantity: ${item.quantity}'),
                      );
                    },
                  ),
                ),
              ],
            ),
          );
        },
      ),
    );
  }
}
