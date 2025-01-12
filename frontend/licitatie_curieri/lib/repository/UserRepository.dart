import 'dart:convert';

import 'package:http/http.dart';

import '../common/GetToken.dart';
import '../common/Utils.dart';
import '../model/User.dart';

class UserRepository {
  final String baseUrl = '${Utils.baseUrl}/users';
  final GetToken getToken = GetToken();

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

  Future<String> verifyUser(String email, String emailVerificationCode,
      String phoneVerificationCode) async {
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
        var jsonResponse = jsonDecode(response.body);
        String token = jsonResponse['token'];
        if (token != null && token.isNotEmpty) {
          return token;
        } else {
          throw Exception('Token not found in response');
        }
    } else {
      throw Exception('Verification failed. ${response.body}');
    }
  }

  Future<bool> authentication(String email, String password) async {
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

    if (response.statusCode == 200) {
      return true;
    } else {
      throw Exception('Authentication failed. ${response.body}');
    }
  }

  Future<String> twoFACode(String email, String verificationCode) async {
    String? token = await getToken.getToken();

    if (token == null) {
      throw Exception("Authentication token not found");
    }

    final response = await put(
      Uri.parse("$baseUrl/getTwoFACode"),
      headers: {
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Bearer $token',
      },
      body: jsonEncode({
        'email': email,
        'verificationCode': verificationCode,
      }),
    );

    if (response.statusCode == 200) {
        var jsonResponse = jsonDecode(response.body);
        String token = jsonResponse['token'];
        if (token != null && token.isNotEmpty) {
          return token;
        } else {
          throw Exception('Token not found in response');
        }
    } else {
      throw Exception('2FA failed. ${response.body}');
    }
  }
}
