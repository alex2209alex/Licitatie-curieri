import 'dart:convert';
import 'dart:developer';
import 'package:flutter/material.dart';
import 'package:licitatie_curieri/restaurant/providers/OrderProvider.dart';
import 'package:provider/provider.dart';
import 'package:stomp_dart_client/stomp_dart_client.dart';

import '../../common/widgets/CartActionBarButton.dart';
import '../models/OrderModel.dart';
import 'OrderDetailsScreen.dart';

class OrdersCourierScreen extends StatefulWidget {
  const OrdersCourierScreen({Key? key}) : super(key: key);

  @override
  State<OrdersCourierScreen> createState() => _OrdersCourierScreenState();
}

class _OrdersCourierScreenState extends State<OrdersCourierScreen> {
  late StompClient _stompClient;
  bool _isConnected = false;

  @override
  void initState() {
    super.initState();

    // Initialize the STOMP client
    _stompClient = StompClient(
      config: StompConfig(
          url: '',
        onConnect: onConnectCallback
      ),
    );

    // Connect to the server
    _connectToStompServer();

    // Initial data fetch
    WidgetsBinding.instance.addPostFrameCallback((_) {
      initData();
    });
  }

  void onConnectCallback(StompFrame connectFrame) {
    log("Listening...");
  }
  Future<void> _connectToStompServer() async {
    try {
      _stompClient.activate();
      log('Connected to STOMP server');
      setState(() {
        _isConnected = true;
      });

      // Subscribe to a destination for real-time updates
      _stompClient.subscribe(
        destination: '/topic/offers',
        callback: (frame) {
          final data = json.decode(frame.body!);
          log('Received update: $data');
          // Update the UI based on the received data
          final orderProvider = Provider.of<OrderProvider>(context, listen: false);
          orderProvider.updateOrderFromRealTime(data);
        },
      );
    } catch (e) {
      log('Failed to connect to STOMP server: $e');
    }
  }

  Future<void> initData() async {
    final orderProvider = Provider.of<OrderProvider>(context, listen: false);
    await orderProvider.fetchOrdersCourier();
  }

  @override
  void dispose() {
    _stompClient.deactivate();
    super.dispose();
  }

  void _makeBid(int orderId, double bidAmount) {
    if (_isConnected) {
      final bidData = {
        'orderId': orderId,
        'deliveryPrice': bidAmount,
      };
      _stompClient.send(
        destination: '/realTime/order/makeOffer',
        body: json.encode(bidData),
      );
      log('Bid sent: $bidData');
    } else {
      log('STOMP client not connected');
    }
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
                          Navigator.push(
                            context,
                            MaterialPageRoute(
                              builder: (context) => OrderDetailsScreen(orderId: order.id),
                            ),
                          );
                        },
                      ),
                      if (order.orderStatus == "IN_AUCTION")
                        IconButton(
                          icon: const Icon(Icons.gavel),
                          onPressed: () {
                            _showBidDialog(order.id);
                          },
                        ),
                    ],
                  ),
                );
              } else {
                return const SizedBox();
              }
            },
          );
        },
      ),
    );
  }

  void _showBidDialog(int orderId) {
    final bidController = TextEditingController();

    showDialog(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: const Text('Place Your Bid'),
          content: TextField(
            controller: bidController,
            keyboardType: TextInputType.number,
            decoration: const InputDecoration(labelText: 'Enter bid amount (RON)'),
          ),
          actions: [
            TextButton(
              onPressed: () {
                Navigator.pop(context);
              },
              child: const Text('Cancel'),
            ),
            TextButton(
              onPressed: () {
                final bidAmount = double.tryParse(bidController.text);
                if (bidAmount != null) {
                  _makeBid(orderId, bidAmount);
                  Navigator.pop(context);
                } else {
                  log('Invalid bid amount');
                }
              },
              child: const Text('Submit'),
            ),
          ],
        );
      },
    );
  }
}
