import 'package:flutter/material.dart';
import 'package:licitatie_curieri/ui/AuthenticatePage.dart';

import '../GetToken.dart';


class LogoutActionBarButton extends StatefulWidget {
  LogoutActionBarButton({
    Key? key
  }) : super(key: key);

  @override
  State<LogoutActionBarButton> createState() => LogoutActionBarButtonState();
}

class LogoutActionBarButtonState extends State<LogoutActionBarButton> {

  void initState() {
    super.initState();
  }

  Future<void> logout() async {
    setState(() {
      GetToken().setToken("");
    });
  }

  @override
  Widget build(BuildContext context) {
    return
      Padding(
        padding: const EdgeInsets.only(top: 12.0, bottom: 12.0, right: 12.0, left: 0.0),
        child: Stack(
          clipBehavior: Clip.none,
          children: [
            FloatingActionButton(
                heroTag: UniqueKey(),

                onPressed: () {
                    setState(() {
                      GetToken().setToken("");
                    });

                    Navigator.pushAndRemoveUntil(
                      context,
                      MaterialPageRoute(builder: (context) => AuthenticatePage()),
                          (Route<dynamic> route) => false
                    );
                },

                child: const Icon(Icons.person_off))
          ],
        ),
      );
  }
}