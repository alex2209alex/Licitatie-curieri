import 'dart:convert';

import '../model/User.dart';
import 'package:http/http.dart';

class UserRepository {
  final String baseUrl = 'http://192.168.100.42:8080/users/signup';

  Future<bool> signUp(User user) async {
    final response = await post(
      Uri.parse(baseUrl),
      headers: {
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*'
      },
      body: jsonEncode(user.toJson()),
    );

    if (response.statusCode == 200) {
      return true;
    } else {
      throw Exception('Failed to sign up user: ${response.body}');
    }
  }
}