import 'package:licitatie_curieri/model/enum/UserType.dart';

class User {
  final String firstName;
  final String lastName;
  final String email;
  final String phoneNumber;
  final String password;
  final String passwordConfirmation;
  final UserType userType;

  User({
    required this.firstName,
    required this.lastName,
    required this.email,
    required this.phoneNumber,
    required this.password,
    required this.passwordConfirmation,
    required this.userType,
  });

  Map<String, dynamic> toJson() {
    return {
      'firstName': firstName,
      'lastName': lastName,
      'email': email,
      'phoneNumber': phoneNumber,
      'password': password,
      'passwordConfirmation': passwordConfirmation,
      'userType': userType.toString().split('.').last,
    };
  }
}
