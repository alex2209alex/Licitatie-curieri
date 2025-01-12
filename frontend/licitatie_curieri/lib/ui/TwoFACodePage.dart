import 'dart:developer';

import 'package:dart_jsonwebtoken/dart_jsonwebtoken.dart';
import 'package:flutter/material.dart';
import 'package:licitatie_curieri/common/GetToken.dart';
import 'package:licitatie_curieri/model/enum/UserType.dart';
import 'package:licitatie_curieri/restaurant/screens/RestaurantsManageScreen.dart';
import 'package:licitatie_curieri/ui/utils/AppColors.dart';
import 'package:licitatie_curieri/ui/utils/Constants.dart';
import 'package:licitatie_curieri/viewModel/UserViewModel.dart';
import 'package:provider/provider.dart';

import '../restaurant/screens/RestaurantsScreen.dart';

class TwoFACodePage extends StatefulWidget {
  const TwoFACodePage({super.key});

  @override
  TwoFACodePageState createState() => TwoFACodePageState();
}

class TwoFACodePageState extends State<TwoFACodePage> {
  final twoFACodeController = TextEditingController();
  bool isLoading = false;

  @override
  Widget build(BuildContext context) {
    final userViewModel = Provider.of<UserViewModel>(context);

    return Scaffold(
      appBar: AppBar(title: Text(Constants.TWO_FA + userViewModel.userEmail)),
      body: Padding(
        padding: const EdgeInsets.all(20),
        child: SingleChildScrollView(
          child: Column(
            children: [
              TextField(
                controller: twoFACodeController,
                decoration: const InputDecoration(
                  labelText: Constants.TWO_FA_CODE,
                  prefixIcon: Icon(Icons.security),
                ),
              ),
              const SizedBox(height: 20),
              ElevatedButton(
                onPressed: isLoading
                    ? null
                    : () async {
                        setState(() {
                          isLoading = true;
                        });

                        try {
                          bool isVerified = await userViewModel
                              .twoFACode(twoFACodeController.text);
                          if (isVerified) {
                            final token = await GetToken().getToken();
                            final jwt = JWT.decode(token!);
                            log("JWT payload: ${jwt.payload}");
                            final clientType = jwt.payload['sub'];
                            log("Clienty Type: $clientType");
                            if (clientType == UserType.CLIENT.name) {
                              Navigator.pushReplacement(
                                context,
                                MaterialPageRoute(
                                    builder: (context) =>
                                        const RestaurantsScreen()),
                              );
                            } else if (clientType ==
                                UserType.RESTAURANT_ADMIN.name) {
                              Navigator.pushReplacement(
                                context,
                                MaterialPageRoute(
                                    builder: (context) =>
                                        const RestaurantsManageScreen()),
                              );
                            }
                            // TODO courier page
                            else if (clientType == UserType.COURIER.name) {
                              Navigator.pushReplacement(
                                context,
                                MaterialPageRoute(
                                    builder: (context) =>
                                        const RestaurantsScreen()),
                              );
                            }
                          } else {
                            ScaffoldMessenger.of(context).showSnackBar(
                                const SnackBar(
                                    content: Text(Constants.TWO_FA_FAILED)));
                          }
                        } catch (e) {
                          ScaffoldMessenger.of(context).showSnackBar(SnackBar(
                              content: Text(
                                  Constants.TWO_FA_FAILED + e.toString())));
                        } finally {
                          setState(() {
                            isLoading = false;
                          });
                        }
                      },
                child: isLoading
                    ? const CircularProgressIndicator(color: AppColors.red)
                    : const Text(Constants.VERIFY),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
