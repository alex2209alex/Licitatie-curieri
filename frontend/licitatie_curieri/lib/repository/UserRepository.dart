import 'dart:convert';

import '../model/User.dart';
import 'package:http/http.dart';

class UserRepository {
  final String baseUrl = 'http://192.168.1.130:8080/users';

  Future<bool> signUp(User user) async {
    final response = await post(
      Uri.parse("$baseUrl/signup"),
      headers: {
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*'
      },
      body: jsonEncode(user.toJson()),
    );

    if (response.statusCode == 201) {
      return true;
    } else {
      throw Exception('Failed to sign up user: ${response.body}');
    }
  }

  Future<bool> verifyUser(
      String email,
      String emailVerificationCode,
      String phoneVerificationCode
      ) async {
    final response = await put(
      Uri.parse("$baseUrl/$email/verification"),
      headers: {
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*'
      },
      body: jsonEncode({
        'emailVerificationCode': emailVerificationCode,
        'phoneVerificationCode': phoneVerificationCode,
      }),
    );

    if (response.statusCode == 200) {
      return true;
    } else {
      throw Exception('Verification failed. ${response.body}');
    }
  }

  Future<bool> authentication (String email, String password) async {
    final response = await post(
      Uri.parse("$baseUrl/login"),
      headers: {
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*'
      },
        body: jsonEncode({
          'email': email,
          'password': password,
        }),
    );

    if (response.statusCode == 201) {
      return true;
    } else {
      throw Exception('Authentication failed. ${response.body}');
    }
  }
}