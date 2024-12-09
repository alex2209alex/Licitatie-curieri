import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:licitatie_curieri/address/models/AddressModel.dart';
import 'package:licitatie_curieri/address/providers/AddressProvider.dart';
import 'dart:convert';
import 'package:provider/provider.dart';

class CreateAddressScreen extends StatefulWidget {
  const CreateAddressScreen({Key? key}) : super(key: key);

  @override
  State<CreateAddressScreen> createState() => _CreateAddressScreenState();
}

class _CreateAddressScreenState extends State<CreateAddressScreen> {
  final TextEditingController _detailsController = TextEditingController();
  final TextEditingController _latitudeController = TextEditingController();
  final TextEditingController _longitudeController = TextEditingController();
  final FocusNode _detailsFocusNode = FocusNode();

  @override
  void initState() {
    super.initState();
    _detailsFocusNode.addListener(() {
      if (!_detailsFocusNode.hasFocus) {
        _fetchCoordinatesForAddress();
      }
    });
  }

  Future<void> _fetchCoordinatesForAddress() async {
    final address = _detailsController.text;
    if (address.isEmpty) return;

    final apiKey = "";
    final url =
        "https://maps.googleapis.com/maps/api/geocode/json?address=${Uri.encodeComponent(address)}&key=$apiKey";

    try {
      final response = await http.get(Uri.parse(url));
      final data = json.decode(response.body);

      if (data['status'] == 'OK') {
        final location = data['results'][0]['geometry']['location'];
        final latitude = location['lat'];
        final longitude = location['lng'];

        setState(() {
          _latitudeController.text = latitude.toString();
          _longitudeController.text = longitude.toString();
        });
      } else {
        _showErrorDialog(context, "Invalid address. Please try again.");
      }
    } catch (e) {
      _showErrorDialog(context, "An error occurred. Please try again.");
    }
  }

  @override
  void dispose() {
    _detailsFocusNode.dispose();
    _detailsController.dispose();
    _latitudeController.dispose();
    _longitudeController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text("Create Address")),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            TextField(
              controller: _detailsController,
              focusNode: _detailsFocusNode,
              decoration: const InputDecoration(labelText: "Details"),
            ),
            TextField(
              controller: _latitudeController,
              decoration: const InputDecoration(labelText: "Latitude"),
              keyboardType: TextInputType.number,
              readOnly: true,
            ),
            TextField(
              controller: _longitudeController,
              decoration: const InputDecoration(labelText: "Longitude"),
              keyboardType: TextInputType.number,
              readOnly: true,
            ),
            const SizedBox(height: 20),
            ElevatedButton(
              onPressed: () async {
                final details = _detailsController.text;
                final latitude = double.tryParse(_latitudeController.text);
                final longitude = double.tryParse(_longitudeController.text);

                if (details.isEmpty || latitude == null || longitude == null) {
                  _showErrorDialog(context, "All fields must be filled correctly.");
                  return;
                }

                final address = Address(
                id: 1,              // IT DOESN'T MATTER
                details: details,
                latitude: latitude,
                longitude: longitude,
                );

                final success = await Provider.of<AddressProvider>(context, listen: false)
                    .createAddress(address);

                if (success) {
                Navigator.pop(context);
                } else {
                  _showErrorDialog(
                      context, "Failed to create address. Please try again.");
                }
              },
              child: const Text("Create Address"),
            ),
          ],
        ),
      ),
    );
  }

  void _showErrorDialog(BuildContext context, String message) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: const Text("Error"),
          content: Text(message),
          actions: [
            TextButton(
              onPressed: () {
                Navigator.of(context).pop();
              },
              child: const Text("OK"),
            ),
          ],
        );
      },
    );
  }
}
