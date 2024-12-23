import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:licitatie_curieri/address/models/AddressModel.dart';
import 'package:licitatie_curieri/address/providers/AddressProvider.dart';
import 'dart:convert';
import 'package:provider/provider.dart';

import '../../common/Utils.dart';

class CreateAddressScreen extends StatefulWidget {
  const CreateAddressScreen({Key? key}) : super(key: key);

  @override
  State<CreateAddressScreen> createState() => _CreateAddressScreenState();
}

class _CreateAddressScreenState extends State<CreateAddressScreen> {
  final TextEditingController _detailsController = TextEditingController();
  final TextEditingController _latitudeController = TextEditingController();
  final TextEditingController _longitudeController = TextEditingController();


  bool isEditing = false;
  bool isValidAddress = false;


  @override
  void initState() {
    super.initState();
  }

  Future<void> _fetchCoordinatesForAddress() async {
    setState(() {
      isValidAddress = false;
    });

    final address = _detailsController.text;
    if (address.isEmpty) return;

    const apiKey = Utils.mapsApiKey;
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
          isValidAddress = true;
        });
      } else {
        setState(() {
          isValidAddress = false;
        });
        _showErrorDialog(context, "Invalid address. Please try again.");
      }
    } catch (e) {
      setState(() {
        isValidAddress = false;
      });
      _showErrorDialog(context, "An error occurred. Please try again.");
    }

    return;
  }

  @override
  void dispose() {
    _detailsController.dispose();
    _latitudeController.dispose();
    _longitudeController.dispose();
    super.dispose();
  }


  Future<void> createAddress() async
  {
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
              onTap: (){
                setState(() {
                  isEditing = true;
                });
              },
              onChanged: (_){
               setState(() {
                 isEditing = true;
               });
              }
              ,
              onEditingComplete: () async
              {
                setState(() {
                  isEditing = false;
                });

                await _fetchCoordinatesForAddress();
              },
              controller: _detailsController,
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
              onPressed: (isEditing || !isValidAddress)? null : createAddress,
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
