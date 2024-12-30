import 'dart:developer';

import 'package:shared_preferences/shared_preferences.dart';

class GetToken {
  Future<String?> getToken() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    String? token = prefs.getString('authToken');

    log("TOKEN IS: $token");

    return token;
  }

  Future<void> setToken(String token) async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    await prefs.setString('authToken', token);

    prefs = await SharedPreferences.getInstance();
    String? tokenOut = prefs.getString('authToken');
    log("TOKEN SET TO: $tokenOut");
  }
}