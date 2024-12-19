import 'package:flutter/foundation.dart';

import '../model/User.dart';
import '../repository/UserRepository.dart';

class UserViewModel extends ChangeNotifier {
  final UserRepository userRepository;
  String userEmail = "null email";

  UserViewModel(this.userRepository);

  Future<bool> signUp(User user) async {
    if(user.password == user.passwordConfirmation) {
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

  Future<bool> verifyUser(
      String emailVerificationCode,
      String phoneVerificationCode
      ) async {

    return await userRepository.verifyUser(
        userEmail,
        emailVerificationCode,
        phoneVerificationCode
    );
  }

  Future<bool> authentication(String email, String password) async {
    return await userRepository.authentication(email, password);
  }
}
