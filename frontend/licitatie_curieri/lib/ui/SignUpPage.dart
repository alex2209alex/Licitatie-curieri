import 'package:flutter/material.dart';
import 'package:flutter_material_pickers/helpers/show_scroll_picker.dart';
import 'package:licitatie_curieri/ui/AuthenticatePage.dart';
import 'package:licitatie_curieri/ui/utils/AppColors.dart';
import 'package:licitatie_curieri/ui/utils/Constants.dart';
import 'package:licitatie_curieri/viewModel/UserViewModel.dart';
import 'package:provider/provider.dart';

import '../model/User.dart';
import '../model/enum/UserType.dart';
import 'VerifyUserPage.dart';

class SignUpPage extends StatefulWidget {
  const SignUpPage({super.key});

  @override
  SignUpPageState createState() => SignUpPageState();
}

class SignUpPageState extends State<SignUpPage> {
  final firstNameController = TextEditingController();
  final lastNameController = TextEditingController();
  final emailController = TextEditingController();
  final phoneNumberController = TextEditingController();
  final passwordController = TextEditingController();
  final passwordConfirmationController = TextEditingController();
  UserType? selectedUserType = UserType.CLIENT;

  bool passwordVisible = false;
  bool confirmPasswordVisible = false;
  bool isLoading = false;

  @override
  Widget build(BuildContext context) {
    final userViewModel = Provider.of<UserViewModel>(context);

    return Scaffold(
      appBar: AppBar(title: const Text(Constants.SIGN_UP)),

      body: Padding(
        padding: const EdgeInsets.all(20),
        child: SingleChildScrollView(
          child: Column(
            children: [
              TextField(
                  controller: firstNameController,
                  decoration: const InputDecoration(labelText: Constants.FIRST_NAME)
              ),

              const SizedBox(height: 10),

              TextField(
                  controller: lastNameController,
                  decoration: const InputDecoration(labelText: Constants.LAST_NAME)
              ),

              const SizedBox(height: 10),

              TextField(
                  controller: emailController,
                  decoration: const InputDecoration(labelText: Constants.EMAIL)
              ),

              const SizedBox(height: 10),

              TextField(
                  controller: phoneNumberController,
                  decoration: const InputDecoration(labelText: Constants.PHONE_NUMBER)
              ),

              const SizedBox(height: 10),

              TextField(
                controller: passwordController,
                obscureText: !passwordVisible,
                decoration: InputDecoration(
                  labelText: Constants.PASSWORD,
                  suffixIcon: IconButton(
                    icon: Icon(passwordVisible ? Icons.visibility : Icons.visibility_off),
                    onPressed: () {
                      setState(() {
                        passwordVisible = !passwordVisible;
                      });
                    },
                  ),
                ),
              ),

              const SizedBox(height: 10),

              TextField(
                controller: passwordConfirmationController,
                obscureText: !confirmPasswordVisible,
                decoration: InputDecoration(
                  labelText: Constants.PASSWORD_CONFIRMATION,
                  suffixIcon: IconButton(
                    icon: Icon(confirmPasswordVisible ? Icons.visibility : Icons.visibility_off),
                    onPressed: () {
                      setState(() {
                        confirmPasswordVisible = !confirmPasswordVisible;
                      });
                    },
                  ),
                ),
              ),

              const SizedBox(height: 30),

              ElevatedButton(
                child: Text(
                    selectedUserType != null ? selectedUserType.toString().split('.').last : Constants.SELECT_USER_TYPE
                ),
                onPressed: () => showUserTypePicker(context),
              ),

              const SizedBox(height: 20),

              ElevatedButton(
                onPressed: isLoading ? null : () async {
                  setState(() {
                    isLoading = true;
                  });

                  User newUser = User(
                    firstName: firstNameController.text,
                    lastName: lastNameController.text,
                    email: emailController.text,
                    phoneNumber: phoneNumberController.text,
                    password: passwordController.text,
                    passwordConfirmation: passwordConfirmationController.text,
                    userType: selectedUserType ?? UserType.CLIENT,
                  );

                  try {
                    bool isSignedUp = await userViewModel.signUp(newUser);
                    if (isSignedUp) {
                      Navigator.pushReplacement(
                        context,
                        MaterialPageRoute(builder: (context) =>VerificationPage(selectedUserType: selectedUserType ?? UserType.CLIENT)),
                      );
                    } else {
                      ScaffoldMessenger.of(context).showSnackBar(
                          const SnackBar(content: Text(Constants.SIGN_UP_FAIL))
                      );
                    }
                  } catch (e) {
                    ScaffoldMessenger.of(context).showSnackBar(
                        SnackBar(content: Text(Constants.SIGN_UP_FAIL + e.toString()))
                    );
                  } finally {
                    setState(() {
                      isLoading = false;
                    });
                  }
                },
                child: isLoading ? const CircularProgressIndicator(color: AppColors.red) : const Text(Constants.SIGN_UP),
              ),

              const SizedBox(height: 20),

              ElevatedButton(
                onPressed: () {
                  Navigator.pushReplacement(
                    context,
                    MaterialPageRoute(builder: (context) => const AuthenticatePage()),  // Navighează către pagina de înregistrare
                  );
                },
                child: const Text(Constants.LOGIN),
              ),
            ],
          ),
        ),
      ),
    );
  }

  void showUserTypePicker(BuildContext context) {
    showMaterialScrollPicker<UserType>(
      context: context,
      title: Constants.SELECT_USER_TYPE,
      items: UserType.values,
      selectedItem: selectedUserType ?? UserType.CLIENT,
      onChanged: (value) => setState(() => selectedUserType = value),
      transformer: (item) => item.toString().split('.').last,
    );
  }
}