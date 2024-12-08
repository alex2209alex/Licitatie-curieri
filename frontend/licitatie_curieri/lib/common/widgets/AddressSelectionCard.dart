import 'package:flutter/material.dart';
import 'package:licitatie_curieri/address/models/AddressModel.dart';


class AddressSelectionCard extends StatelessWidget {
  final Address address;
  final bool isSelected;
  final VoidCallback onSelect;

  const AddressSelectionCard({
    Key? key,
    required this.address,
    required this.isSelected,
    required this.onSelect,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onSelect,
      child: Card(
        color: isSelected ? Colors.greenAccent : Colors.white,
        child: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text("Details: ${address.details}", style: TextStyle(fontSize: 16)),
              Text("Latitude: ${address.latitude}", style: TextStyle(fontSize: 14)),
              Text("Longitude: ${address.longitude}", style: TextStyle(fontSize: 14)),
              if (isSelected)
                const Text(
                  "Selected",
                  style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold),
                ),
            ],
          ),
        ),
      ),
    );
  }
}
