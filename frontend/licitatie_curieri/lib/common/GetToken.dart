import 'package:shared_preferences/shared_preferences.dart';

class GetToken {
  Future<String?> getToken() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    String? token = prefs.getString('authToken');
    return token;
  }
}