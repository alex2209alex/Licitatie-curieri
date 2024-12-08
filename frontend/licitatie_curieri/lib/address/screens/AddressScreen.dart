import 'package:flutter/material.dart';
import 'package:licitatie_curieri/common/widgets/AddressSelectionCard.dart';
import 'package:provider/provider.dart';
import 'package:licitatie_curieri/address/providers/AddressProvider.dart';
import 'CreateAddressScreen.dart';

class AddressScreen extends StatefulWidget {
  const AddressScreen({Key? key}) : super(key: key);

  @override
  State<AddressScreen> createState() => _AddressScreenState();
}

class _AddressScreenState extends State<AddressScreen> {
  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      Provider.of<AddressProvider>(context, listen: false).fetchAddressesByUserId(1); // Put actual User ID
    });
  }

  @override
  Widget build(BuildContext context) {
    final addressProvider = Provider.of<AddressProvider>(context);

    return Scaffold(
      appBar: AppBar(title: const Text("Addresses")),
      body: addressProvider.isLoading
          ? const Center(child: CircularProgressIndicator())
          : ListView.builder(
        itemCount: addressProvider.addresses.length,
        itemBuilder: (context, index) {
          final address = addressProvider.addresses[index];
          return AddressSelectionCard(
            address: address,
            isSelected: addressProvider.selectedAddress == address,
            onSelect: () => addressProvider.setSelectedAddress(address),
          );
        },
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          Navigator.push(
            context,
            MaterialPageRoute(builder: (context) => const CreateAddressScreen()),
          ).then((_)
          {
            Provider.of<AddressProvider>(context, listen: false).fetchAddressesByUserId(1);
          });
        },
        child: const Icon(Icons.add),
      ),
    );
  }
}
