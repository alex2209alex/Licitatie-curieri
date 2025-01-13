import 'package:flutter/cupertino.dart';

import '../model/PaymentIntentData.dart';
import '../repository/PaymentRepository.dart';

class PaymentViewModel extends ChangeNotifier {
  final PaymentRepository paymentRepository;
  PaymentIntentData? paymentIntentData;

  PaymentViewModel(this.paymentRepository);

  Future<void> initPayment(String amount, String currency) async {
    paymentIntentData = await paymentRepository.createPaymentIntent(amount, currency);

    if (paymentIntentData != null) {
      // ???
    } else {
      // ??
    }
  }
}