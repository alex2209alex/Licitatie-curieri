import 'package:flutter/foundation.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../model/User.dart';
import '../repository/UserRepository.dart';

class UserViewModel extends ChangeNotifier {
  final UserRepository userRepository;
  String userEmail = "null email";

  UserViewModel(this.userRepository);

  Future<bool> signUp(User user) async {
    if (user.password == user.passwordConfirmation) {
      bool isSignedUp = await userRepository.signUp(user);

      if (isSignedUp) {
        userEmail = user.email;
        //notifyListeners();
      }
      return isSignedUp;
    } else {
      throw ("Passwords are not identical");
    }
  }

  Future<bool> verifyUser(String emailVerificationCode, String phoneVerificationCode) async {
    String token = await userRepository.verifyUser(
        userEmail, emailVerificationCode, phoneVerificationCode);

    if (token != null && token.isNotEmpty) {
      SharedPreferences prefs = await SharedPreferences.getInstance();
      await prefs.setString('authToken', token);
      return true;
    } else {
      return false;
    }
  }

  Future<bool> authentication(String email, String password) async {
    userEmail = email;
    return await userRepository.authentication(email, password);
  }

  Future<bool> twoFACode(String verificationCode) async {
    String token = await userRepository.twoFACode(userEmail, verificationCode);

    if (token != null && token.isNotEmpty) {
      SharedPreferences prefs = await SharedPreferences.getInstance();
      await prefs.setString('authToken', token);
      return true;
    } else {
      return false;
    }
  }
}
