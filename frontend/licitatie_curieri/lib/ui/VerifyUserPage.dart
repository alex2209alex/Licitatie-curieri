import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:licitatie_curieri/ui/utils/AppColors.dart';
import 'package:licitatie_curieri/ui/utils/Constants.dart';
import 'package:provider/provider.dart';

import '../restaurant/screens/RestaurantsScreen.dart';
import '../viewModel/UserViewModel.dart';

class VerificationPage extends StatefulWidget {
  const VerificationPage({super.key});

  @override
  VerificationPageState createState() => VerificationPageState();
}

class VerificationPageState extends State<VerificationPage> {
  final emailVerificationCodeController = TextEditingController();
  final phoneVerificationCodeController = TextEditingController();
  bool isLoading = false;

  @override
  Widget build(BuildContext context) {
    final userViewModel = Provider.of<UserViewModel>(context);

    return Scaffold(
      appBar: AppBar(
        title: Text('Verify ${userViewModel.userEmail}'),
      ),

      body: Padding(
        padding: const EdgeInsets.all(20),

        child: SingleChildScrollView(
          child: Column(
            children: [
              TextField(
                controller: emailVerificationCodeController,
                decoration: const InputDecoration(labelText: Constants.EMAIL_VERIFICATION_CODE),
                keyboardType: TextInputType.number,
              ),

              const SizedBox(height: 10),

              TextField(
                controller: phoneVerificationCodeController,
                decoration: const InputDecoration(labelText: Constants.PHONE_VERIFICATION_CODE),
                keyboardType: TextInputType.number,
              ),

              const SizedBox(height: 20),

              ElevatedButton(
                onPressed: isLoading ? null : () async {
                  setState(() {
                    isLoading = true;
                  });

                  try {
                    bool isVerified = await userViewModel.verifyUser(
                      emailVerificationCodeController.text,
                      phoneVerificationCodeController.text,
                    );

                    if (isVerified) {
                      // navigate to main page
                      Navigator.pushReplacement(
                        context,
                        MaterialPageRoute(builder: (context) => RestaurantsScreen()),
                      );
                    } else {
                      ScaffoldMessenger.of(context).showSnackBar(
                          const SnackBar(content: Text(Constants.VERIFY_FAIL))
                      );
                    }
                  } catch (e) {
                    ScaffoldMessenger.of(context).showSnackBar(
                      SnackBar(content: Text(Constants.VERIFY_FAIL + e.toString())),
                    );
                  } finally {
                    setState(() {
                      isLoading = false;
                    });
                  }
                },
                child: isLoading ? const CircularProgressIndicator(color: AppColors.red) : const Text(Constants.VERIFY),
              ),
            ],
          ),
        ),
      ),
    );
  }
}