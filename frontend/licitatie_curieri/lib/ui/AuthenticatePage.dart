import 'package:flutter/material.dart';
import 'package:licitatie_curieri/ui/utils/AppColors.dart';
import 'package:licitatie_curieri/ui/utils/Constants.dart';
import 'package:licitatie_curieri/viewModel/UserViewModel.dart';
import 'package:provider/provider.dart';
import '../restaurant/screens/RestaurantsScreen.dart';
import 'SignUpPage.dart';
import 'VerifyUserPage.dart';

class AuthenticatePage extends StatefulWidget {
  const AuthenticatePage({super.key});

  @override
  AuthenticatePageState createState() => AuthenticatePageState();
}

class AuthenticatePageState extends State<AuthenticatePage> {
  final emailController = TextEditingController();
  final passwordController = TextEditingController();

  bool passwordVisible = false;
  bool isLoading = false;

  @override
  Widget build(BuildContext context) {
    final userViewModel = Provider.of<UserViewModel>(context);

    return Scaffold(
      appBar: AppBar(title: const Text(Constants.LOGIN)),

      body: Padding(
        padding: const EdgeInsets.all(20),
        child: SingleChildScrollView(
          child: Column(
            children: [
              TextField(
                controller: emailController,
                decoration: const InputDecoration(
                  labelText: Constants.EMAIL,
                  prefixIcon: Icon(Icons.email),
                ),
              ),

              const SizedBox(height: 10),

              TextField(
                controller: passwordController,
                obscureText: !passwordVisible,
                decoration: InputDecoration(
                  labelText: Constants.PASSWORD,
                  prefixIcon: Icon(Icons.lock),
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

              const SizedBox(height: 20),

              ElevatedButton(
                onPressed: isLoading ? null : () async {
                  setState(() {
                    isLoading = true;
                  });

                  try {
                    bool isLoggedIn = await userViewModel.authentication(emailController.text, passwordController.text);
                    if (isLoggedIn) {
                      Navigator.pushReplacement(
                        context,
                        MaterialPageRoute(builder: (context) => const RestaurantsScreen()),
                      );
                    } else {
                      ScaffoldMessenger.of(context).showSnackBar(
                          const SnackBar(content: Text(Constants.LOGIN_FAIL))
                      );
                    }
                  } catch (e) {
                    ScaffoldMessenger.of(context).showSnackBar(
                        SnackBar(content: Text(Constants.LOGIN_FAIL + e.toString()))
                    );
                  } finally {
                    setState(() {
                      isLoading = false;
                    });
                  }
                },
                child: isLoading ? const CircularProgressIndicator(color: AppColors.red) : const Text(Constants.LOGIN),
              ),

              const SizedBox(height: 20),

              ElevatedButton(
                onPressed: () {
                  Navigator.pushReplacement(
                    context,
                    MaterialPageRoute(builder: (context) => const SignUpPage()),  // Navighează către pagina de înregistrare
                  );
                },
                child: const Text(Constants.SIGN_UP),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
