import 'dart:convert';

import 'package:http/http.dart';

import '../common/GetToken.dart';
import '../common/Utils.dart';
import '../model/PaymentIntentData.dart';

class PaymentRepository {
  final String baseUrl = '${Utils.BASE_URL}/payment';
  final GetToken getToken = GetToken();

  Future<PaymentIntentData?> createPaymentIntent(String amount, String currency) async {
    String? token = await getToken.getToken();

    if (token == null) {
      throw Exception("Authentication token not found");
    }

    final response = await post(
        Uri.parse("$baseUrl/createIntent"),
        headers: {
          'Content-Type': 'application/json',
          'Access-Control-Allow-Origin': '*',
          'Authorization': 'Bearer $token',
        },
        body: jsonEncode({
          'amount': amount,
          'currency': currency
        })
    );

    if (response.statusCode == 200) {
      var jsonResponse = jsonDecode(response.body);
      return PaymentIntentData(
        clientSecret: jsonResponse['clientSecret'],
        paymentIntentId: jsonResponse['paymentIntentId'],
      );
    } else {
      throw Exception("PaymentIntentData not found");
    }
  }
}