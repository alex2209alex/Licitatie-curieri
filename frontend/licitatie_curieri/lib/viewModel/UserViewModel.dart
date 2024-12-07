import 'package:flutter/foundation.dart';

import '../model/User.dart';
import '../repository/UserRepository.dart';

class UserViewModel extends ChangeNotifier {
  final UserRepository userRepository;

  UserViewModel(this.userRepository);

  Future<bool> signUp(User user) async {
    return await userRepository.signUp(user);
  }
}
