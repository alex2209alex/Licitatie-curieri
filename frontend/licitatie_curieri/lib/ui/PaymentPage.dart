import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_stripe/flutter_stripe.dart';
import 'package:provider/provider.dart';
import '../model/PaymentIntentData.dart';
import '../viewModel/PaymentViewModel.dart';

class PaymentPage extends StatefulWidget {
  const PaymentPage({super.key});

  @override
  PaymentPageState createState() => PaymentPageState();
}

class PaymentPageState extends State<PaymentPage> {
  bool isLoading = false;

  @override
  Widget build(BuildContext context) {
    final paymentViewModel = Provider.of<PaymentViewModel>(context);

    return Scaffold(
      appBar: AppBar(title: const Text("Complete your payment")),
      body: Padding(
        padding: const EdgeInsets.all(20),
        child: SingleChildScrollView(
          child: Column(
            children: [
              const SizedBox(height: 20),
              ElevatedButton(
                onPressed: isLoading
                    ? null
                    : () async {
                  setState(() {
                    isLoading = true;
                  });

                  try {
                    await paymentViewModel.initPayment('1000', 'ron'); // trb modificat aici. momentan hardcodat
                    if (paymentViewModel.paymentIntentData != null) {
                      await showPaymentSheet(context, paymentViewModel.paymentIntentData!);
                    }
                  } catch (e) {
                    ScaffoldMessenger.of(context).showSnackBar(SnackBar(
                        content: Text("Payment failed: ${e.toString()}")));
                  } finally {
                    setState(() {
                      isLoading = false;
                    });
                  }
                },
                child: isLoading
                    ? const CircularProgressIndicator()
                    : const Text("Pay with Stripe"),
              ),
            ],
          ),
        ),
      ),
    );
  }

  Future<void> showPaymentSheet(BuildContext context, PaymentIntentData intentData) async {
    try {
      await Stripe.instance.initPaymentSheet(
        paymentSheetParameters: SetupPaymentSheetParameters(
          paymentIntentClientSecret: intentData.clientSecret,
          merchantDisplayName: 'Your Business Name',
        ),
      );
      await Stripe.instance.presentPaymentSheet();
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text("Paid successfully")));
    } catch (e) {
      debugPrint('Payment failed: ${e.toString()}'); // trb sters
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text("Payment failed: ${e.toString()}")));
    }
  }
}
