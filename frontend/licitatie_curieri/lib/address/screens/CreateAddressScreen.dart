import 'package:flutter/material.dart';
import 'package:licitatie_curieri/address/models/AddressModel.dart';
import 'package:provider/provider.dart';
import 'package:licitatie_curieri/address/providers/AddressProvider.dart';

class CreateAddressScreen extends StatefulWidget {
  const CreateAddressScreen({Key? key}) : super(key: key);

  @override
  State<CreateAddressScreen> createState() => _CreateAddressScreenState();
}

class _CreateAddressScreenState extends State<CreateAddressScreen> {
  final TextEditingController _detailsController = TextEditingController();
  final TextEditingController _latitudeController = TextEditingController();
  final TextEditingController _longitudeController = TextEditingController();

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
              decoration: const InputDecoration(labelText: "Details"),
            ),
            TextField(
              controller: _latitudeController,
              decoration: const InputDecoration(labelText: "Latitude"),
              keyboardType: TextInputType.number,
            ),
            TextField(
              controller: _longitudeController,
              decoration: const InputDecoration(labelText: "Longitude"),
              keyboardType: TextInputType.number,
            ),
            const SizedBox(height: 20),
            ElevatedButton(
              onPressed: () async {
                final details = _detailsController.text;
                final latitude = double.tryParse(_latitudeController.text);
                final longitude = double.tryParse(_longitudeController.text);

                // Validare date
                if (details.isEmpty || latitude == null || longitude == null) {
                  _showErrorDialog(context, "All fields must be filled correctly.");
                  return;
                }

                final address = Address(
                  id: 1, // Temporar; poate fi ignorat dacă backend-ul generează ID-ul.
                  details: details,
                  latitude: latitude,
                  longitude: longitude,
                );

                final success = await Provider.of<AddressProvider>(context, listen: false)
                    .createAddress(address);

                if (success) {
                  Navigator.pop(context); // Închide ecranul dacă operația reușește.
                } else {
                  _showErrorDialog(context, "Failed to create address. Please try again.");
                }
              },
              child: const Text("Create Address"),
            ),
          ],
        ),
      ),
    );
  }

  // Funcție pentru afișarea unui popup cu mesaj de eroare
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
                Navigator.of(context).pop(); // Închide dialogul.
              },
              child: const Text("OK"),
            ),
          ],
        );
      },
    );
  }
}