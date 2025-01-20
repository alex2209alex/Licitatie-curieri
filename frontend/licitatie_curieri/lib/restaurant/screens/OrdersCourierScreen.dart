import 'package:flutter/material.dart';
import 'package:licitatie_curieri/restaurant/screens/OrderDetailsScreen.dart';
import 'package:provider/provider.dart';
import 'package:licitatie_curieri/restaurant/providers/OrderProvider.dart';

import '../models/OrderModel.dart';

class OrdersCourierScreen extends StatefulWidget {
  @override
  _OrdersCourierScreenState createState() => _OrdersCourierScreenState();
}

class _OrdersCourierScreenState extends State<OrdersCourierScreen> {
  @override
  void initState() {
    super.initState();
    final orderProvider = Provider.of<OrderProvider>(context, listen: false);
    WidgetsBinding.instance.addPostFrameCallback((_) async {
      await orderProvider.fetchOrdersCourier();
      orderProvider.connectToWebSocket();
    });
  }

  @override
  void dispose() {
    final orderProvider = Provider.of<OrderProvider>(context, listen: false);
    orderProvider.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final orderProvider = Provider.of<OrderProvider>(context);

    return Scaffold(
      appBar: AppBar(title: Text('Courier Orders')),
      body: orderProvider.isLoading
          ? Center(child: CircularProgressIndicator())
          : ListView.builder(
        itemCount: orderProvider.orders.length,
        itemBuilder: (context, index) {
          final order = orderProvider.orders[index] as OrderDetails;

          return Card(
            margin: EdgeInsets.all(8.0),
            child: Padding(
              padding: const EdgeInsets.all(16.0),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    'Order #${order.id}',
                    style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                  ),
                  SizedBox(height: 8),
                  Text('Restaurant Address: ${order.restaurantAddress}'),
                  Text('Delivery Address: ${order.clientAddress}'),
                  Text('Lowest Bid: ${order.lowestBid}'),
                  Text('Delivery Limit: ${order.deliveryPriceLimit}'),
                  Text('Status: ${order.orderStatus}'),
                  SizedBox(height: 16),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      ElevatedButton.icon(
                        onPressed: () => _showBidDialog(context, order.id),
                        icon: Icon(Icons.gavel),
                        label: Text('Bid'),
                      ),
                      ElevatedButton.icon(
                        onPressed: () => _showDetailsPage(context, order.id),
                        icon: Icon(Icons.details),
                        label: Text('Details'),
                      ),
                    ],
                  ),
                ],
              ),
            ),
          );
        },
      ),
    );
  }

  void _showBidDialog(BuildContext context, int orderId) {
    final bidController = TextEditingController();
    final orderProvider = Provider.of<OrderProvider>(context, listen: false);

    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: Text('Place Bid'),
        content: TextField(
          controller: bidController,
          keyboardType: TextInputType.number,
          decoration: InputDecoration(labelText: 'Bid Amount'),
        ),
        actions: [
          TextButton(
            onPressed: () {
              Navigator.pop(context);
            },
            child: Text('Cancel'),
          ),
          TextButton(
            onPressed: () {
              final bidAmount = double.tryParse(bidController.text);
              if (bidAmount != null) {
                orderProvider.sendBid(orderId, bidAmount);
              }
              Navigator.pop(context);
            },
            child: Text('Submit'),
          ),
        ],
      ),
    );
  }

  void _showDetailsPage(BuildContext context, int orderId) {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (context) => OrderDetailsScreen(orderId: orderId),
      ),
    );
  }
}
