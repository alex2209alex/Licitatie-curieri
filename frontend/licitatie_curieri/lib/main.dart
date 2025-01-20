import 'package:device_preview/device_preview.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_stripe/flutter_stripe.dart';
import 'package:licitatie_curieri/address/providers/AddressProvider.dart';
import 'package:licitatie_curieri/repository/PaymentRepository.dart';
import 'package:licitatie_curieri/repository/UserRepository.dart';
import 'package:licitatie_curieri/restaurant/providers/CartProvider.dart';
import 'package:licitatie_curieri/restaurant/providers/MenuItemProvider.dart';
import 'package:licitatie_curieri/restaurant/providers/OrderProvider.dart';
import 'package:licitatie_curieri/restaurant/providers/RestaurantProvider.dart';
import 'package:licitatie_curieri/restaurant/screens/OrdersCourierScreen.dart';
import 'package:licitatie_curieri/viewModel/PaymentViewModel.dart';
import 'package:licitatie_curieri/viewModel/UserViewModel.dart';
import 'package:provider/provider.dart';

import 'common/Utils.dart';

/*
For errors at payment method
https://stackoverflow.com/questions/70856638/platform-exception-in-stripe-flutter

For payment implementation
https://docs.stripe.com/payments/quickstart

For payment method test in test mode
https://docs.stripe.com/testing?testing-method=card-numbers
*/

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  Stripe.publishableKey = Utils.STRIPE_PUBLISHABLE_KEY;

  runApp(
    DevicePreview(
      enabled: !kReleaseMode, // Disable DevicePreview in release mode
      builder: (context) => MultiProvider(
        providers: [
          ChangeNotifierProvider(create: (_) => RestaurantProvider()),
          ChangeNotifierProvider(create: (_) => MenuItemProvider()),
          ChangeNotifierProvider(create: (_) => CartProvider()),
          ChangeNotifierProvider(create: (_) => AddressProvider()),
          ChangeNotifierProvider(create: (_) => OrderProvider()),
          ChangeNotifierProvider<PaymentViewModel>(
              create: (_) => PaymentViewModel(PaymentRepository()),
          ),
          ChangeNotifierProvider<UserViewModel>(
            create: (_) => UserViewModel(UserRepository()),
          ),
        ],
        child: const MyApp(),
      ),
    ),
  );
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'LicitatieCurieri',
      home: OrdersCourierScreen(),
      locale: DevicePreview.locale(context),
      builder: DevicePreview.appBuilder,
    );
  }
}
