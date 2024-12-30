import 'dart:convert';
import 'dart:developer';
import 'package:http/http.dart' as http;
import 'package:licitatie_curieri/address/models/AddressModel.dart';

import '../../common/GetToken.dart';
import '../../common/Utils.dart';

class AddressService {
  static const String baseUrl = '${Utils.baseUrl}/addresses';
  final GetToken getToken = GetToken();

  static List<Address> addresses = [];

  Future<List<Address>> fetchAddresses() async {
    String? token = await getToken.getToken();

    if (token == null) {
      throw Exception("Authentication token not found");
    }

    log("AUTH TOKEN: ${token.toString()}");
    Uri uri = Uri.parse(baseUrl);
    final response = await http.get(
      uri,
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $token',
      },
    );

    if (response.statusCode == 200) {
      final List<dynamic> data = json.decode(response.body);
      return data.map((json) => Address.fromJson(json)).toList();
    } else {
      throw Exception("Failed to fetch addresses");
    }
  }


  // TO BE MODIFIED
  // cand o sa fie gata partea cu securitatea

  Future<List<Address>> fetchAddressesByUserId(int userId) async {
    String? token = await getToken.getToken();

    if (token == null) {
      throw Exception("Authentication token not found");
    }

    Uri uri = Uri.parse("$baseUrl/user/$userId");
    final response = await http.get(
      uri,
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $token',
      },
    );

    if (response.statusCode == 200) {
      final List<dynamic> data = json.decode(response.body);
      return data.map((json) => Address.fromJson(json)).toList();
    } else {
      throw Exception("Failed to fetch addresses for user $userId");
    }
  }

  Future<Address> fetchAddressById(int id) async {
    String? token = await getToken.getToken();

    if (token == null) {
      throw Exception("Authentication token not found");
    }

    Uri uri = Uri.parse("$baseUrl/$id");
    final response = await http.get(
      uri,
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $token',
      },
    );

    if (response.statusCode == 200) {
      return Address.fromJson(json.decode(response.body));
    } else {
      throw Exception("Failed to fetch address $id");
    }
  }

  Future<Address> createAddress(Address address) async {
    String? token = await getToken.getToken();

    if (token == null) {
      throw Exception("Authentication token not found");
    }

    Uri uri = Uri.parse(baseUrl);
    log("AUTH TOKEN: ${token.toString()}");
    final response = await http.post(
      uri,
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $token',
      },
      body: json.encode(address.toJson()),
    );

    if (response.statusCode == 201) {
      return Address.fromJson(json.decode(response.body));
    } else {
      throw Exception("Failed to create address, response " + response.statusCode.toString());
    }
  }
}