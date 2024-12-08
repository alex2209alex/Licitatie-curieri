import 'package:device_preview/device_preview.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:licitatie_curieri/repository/UserRepository.dart';
import 'package:licitatie_curieri/ui/SignUpPage.dart';
import 'package:licitatie_curieri/viewModel/UserViewModel.dart';
import 'package:provider/provider.dart';

void main() {
  runApp(
    DevicePreview(
      enabled: !kReleaseMode,  // dezactiveaza DevicePreview la release
      builder: (context) => Provider<UserViewModel>(
        create: (_) => UserViewModel(UserRepository()),
        child: MaterialApp(
          home: SignUpPage(),
          useInheritedMediaQuery: true,
          locale: DevicePreview.locale(context),
          builder: DevicePreview.appBuilder,
        ),
      ),
    ),
  );
}
