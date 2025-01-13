import 'package:flutter/cupertino.dart';
import 'package:licitatie_curieri/restaurant/services/OrderService.dart';

import '../../address/providers/AddressProvider.dart';
import '../models/OrderModel.dart';
import 'CartProvider.dart';

class OrderProvider with ChangeNotifier {
  List<Order> _orders = [];
  Order? _selectedOrder;
  bool _isLoading = false;

  List<Order> get orders => _orders;

  Order? get selectedOrder => _selectedOrder;

  bool get isLoading => _isLoading;

  set selectedOrder(Order? order) {
    _selectedOrder = order;
    notifyListeners();
  }

  Future<void> fetchOrdersClient() async {
    _isLoading = true;
    notifyListeners();
    try {
      _orders = await OrderService().fetchOrders();
    } catch (error) {
      print("Error fetching orders: $error");
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<void> fetchOrdersCourier() async {
    _isLoading = true;
    notifyListeners();
    try {
      _orders = await OrderService().fetchOrdersCourier();
    } catch (error) {
      print("Error fetching orders: $error");
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<void> cancelOrder(int id) async {
    _isLoading = true;
    notifyListeners();
    try {
      await OrderService().cancelOrder(id);
      await fetchOrdersClient();
    } catch (error) {
      print("Error cancelling order $id: $error");
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<OrderToDeliverDetails?> fetchOrderDetails(int orderId) async {
    _isLoading = true;
    OrderToDeliverDetails? orderToDeliverDetails;
    notifyListeners();
    try {
      orderToDeliverDetails = await OrderService().fetchOrderDetails(orderId);
    } catch (error) {
      print("Error fetching orderDetails $orderId: $error");
    } finally {
      _isLoading = false;
      notifyListeners();
    }
    return orderToDeliverDetails;
  }

  Future<bool> createOrder(AddressProvider addressProvider,
      double deliveryPriceLimit, CartProvider cartProvider) async {
    _isLoading = true;
    notifyListeners();
    bool isOk = false;
    try {
      isOk = await OrderService()
          .createOrder(addressProvider, deliveryPriceLimit, cartProvider);
      await fetchOrdersClient();
    } catch (error) {
      print("Error creating order: $error");
    } finally {
      _isLoading = false;
      notifyListeners();
      return isOk;
    }
  }

  void setSelectedOrder(Order order) {
    _selectedOrder = order;
    notifyListeners();
  }

  void updateOrderFromRealTime(Map<String, dynamic> data) {
    final orderId = data['orderId'];
    final newOrderStatus = data['orderStatus'];
    final newPrice = data['deliveryPrice'];

    final orderIndex = _orders.indexWhere((order) => order.id == orderId);
    if (orderIndex != -1) {
      (_orders[orderIndex] as OrderDetails).orderStatus = newOrderStatus;
      (_orders[orderIndex] as OrderDetails).lowestBid = newPrice;
      notifyListeners();
    } else {
      print('Order not found: $orderId');
    }
  }

// Future<void> placeBid(int orderId, double bidAmount) async {
//   try {
//     final success = await OrderService().placeBid(orderId, bidAmount);
//     if (success) {
//       print('Bid placed successfully');
//     } else {
//       print('Failed to place bid');
//     }
//   } catch (e) {
//     print('Error placing bid: $e');
//   }
// }
}
