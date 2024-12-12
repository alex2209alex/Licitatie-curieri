import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:licitatie_curieri/address/models/AddressModel.dart';

class AddressService {
  static const String baseUrl = "http://192.168.1.130:8080/addresses";

  static List<Address> addresses = [];

  Future<List<Address>> fetchAddresses() async {
    Uri uri = Uri.parse(baseUrl);
    final response = await http.get(uri);

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
    Uri uri = Uri.parse("$baseUrl/user/$userId");
    final response = await http.get(uri);

    if (response.statusCode == 200) {
      final List<dynamic> data = json.decode(response.body);
      return data.map((json) => Address.fromJson(json)).toList();
    } else {
      throw Exception("Failed to fetch addresses for user $userId");
    }
  }
  Future<Address> fetchAddressById(int id) async {
    Uri uri = Uri.parse("$baseUrl/$id");
    final response = await http.get(uri);
    if (response.statusCode == 200) {
      return Address.fromJson(json.decode(response.body));
    } else {
      throw Exception("Failed to fetch address $id");
    }
  }

  Future<Address> createAddress(Address address) async {
    Uri uri = Uri.parse(baseUrl);
    final response = await http.post(
      uri,
      headers: {'Content-Type': 'application/json'},
      body: json.encode(address.toJson()),
    );

    print("adresa pt post: " + json.encode(address.toJson()));

    if (response.statusCode == 201) {
      return Address.fromJson(json.decode(response.body));
    } else {
      throw Exception("Failed to create address, response " + response.statusCode.toString());
    }
  }
}