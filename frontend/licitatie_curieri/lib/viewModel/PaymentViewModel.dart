import 'package:flutter/cupertino.dart';

import '../model/PaymentIntentData.dart';
import '../repository/PaymentRepository.dart';

class PaymentViewModel extends ChangeNotifier {
  final PaymentRepository paymentRepository;
  PaymentIntentData? paymentIntentData;
  // bool isLoading = false;

  PaymentViewModel(this.paymentRepository);

  Future<void> initPayment(String amount, String currency) async {
    // isLoading = true;
    // notifyListeners();
    paymentIntentData = await paymentRepository.createPaymentIntent(amount, currency);
    // isLoading = false;
    // notifyListeners();

    if (paymentIntentData != null) {
      // procedează la afișarea PaymentSheet
    } else {
      // gestionarea erorii
    }
  }
}