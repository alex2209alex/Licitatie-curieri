class PaymentIntentData {
  final String clientSecret;
  final String paymentIntentId;

  PaymentIntentData({
    required this.clientSecret,
    required this.paymentIntentId
  });
}
