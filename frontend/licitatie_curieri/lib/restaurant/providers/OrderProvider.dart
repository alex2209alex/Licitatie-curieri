import 'package:flutter/cupertino.dart';
import 'package:http/http.dart' as http;
import 'package:licitatie_curieri/restaurant/services/OrderService.dart';

import '../models/OrderModel.dart';

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
    try{
      _orders = await OrderService().fetchOrders();
    }catch(error) {
      print("Error fetching orders: $error");
    } finally{
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
    } catch(error)
    {
      print("Error cancelling order $id: $error");
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  void setSelectedOrder(Order order) {
    _selectedOrder = order;
    notifyListeners();
  }

}
