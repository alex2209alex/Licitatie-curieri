import 'package:flutter/material.dart';
import 'package:device_preview/device_preview.dart';
import 'package:flutter/foundation.dart';
import 'package:licitatie_curieri/restaurant/providers/CartProvider.dart';
import 'package:licitatie_curieri/address/providers/AddressProvider.dart';
import 'package:licitatie_curieri/restaurant/providers/MenuItemProvider.dart';
import 'package:licitatie_curieri/restaurant/providers/RestaurantProvider.dart';
import 'package:licitatie_curieri/ui/AuthenticatePage.dart';
import 'package:provider/provider.dart';
import 'package:licitatie_curieri/repository/UserRepository.dart';
import 'package:licitatie_curieri/viewModel/UserViewModel.dart';

void main() {
  runApp(
    DevicePreview(
      enabled: !kReleaseMode,  // Disable DevicePreview in release mode
      builder: (context) => MultiProvider(
        providers: [
          ChangeNotifierProvider(create: (_) => RestaurantProvider()),
          ChangeNotifierProvider(create: (_) => MenuItemProvider()),
          ChangeNotifierProvider(create: (_) => CartProvider()),
          ChangeNotifierProvider(create: (_) => AddressProvider()),
          ChangeNotifierProvider<UserViewModel>(create: (_) => UserViewModel(UserRepository()),
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
      home: AuthenticatePage(),
      locale: DevicePreview.locale(context),
      builder: DevicePreview.appBuilder,
    );
  }
}



