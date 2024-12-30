import 'dart:developer';

import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../common/Utils.dart';
import '../models/MenuItemModel.dart';
import '../providers/MenuItemProvider.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

class MenusManageScreen extends StatefulWidget {
  final int restaurantId;

  const MenusManageScreen({Key? key, required this.restaurantId}) : super(key: key);

  @override
  State<MenusManageScreen> createState() => _MenusManageScreenState();

}

class _MenusManageScreenState extends State<MenusManageScreen> {
  final _formKey = GlobalKey<FormState>();
  String? _name;

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      Provider.of<MenuItemProvider>(context, listen: false).fetchMenuItems(widget.restaurantId);
    });
  }

  void _showAddEditDialog(BuildContext context, {MenuItem? menuItem}) {

    final TextEditingController _priceController = TextEditingController();
    final TextEditingController _ingredientsListController = TextEditingController();
    final TextEditingController _photoController = TextEditingController();
    final TextEditingController _discountController = TextEditingController();
    bool isEditing = false;

    if (menuItem != null) {
      _name = menuItem.name;
      _priceController.text = "";
      _ingredientsListController.text = "";
      _photoController.text = "";
      _discountController.text = "";
    } else {
      _name = null;
      _priceController.clear();
      _ingredientsListController.clear();
      _photoController.clear();
      _discountController.clear();
    }

    showDialog(
      context: context,
      builder: (ctx) => StatefulBuilder(
        builder: (ctx, setState) => AlertDialog(
          title: Text(menuItem == null ? "Add Menu Item" : "Edit Menu Item"),
          content: Form(
            key: _formKey,
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                TextFormField(
                  initialValue: _name,
                  decoration: const InputDecoration(labelText: "Name"),
                  validator: (value) {
                    if (value == null || value.isEmpty) {
                      return "Please enter a name.";
                    }
                    return null;
                  },
                  onSaved: (value) => _name = value,
                ),
                TextField(
                  controller: _priceController,
                  decoration: const InputDecoration(labelText: "Price")
                ),
                TextField(
                  controller: _ingredientsListController,
                  decoration: const InputDecoration(labelText: "Ingredients")
                ),
                TextField(
                  controller: _photoController,
                  decoration: const InputDecoration(labelText: "Photo URL")
                ),
                TextField(
                    controller: _discountController,
                    decoration: const InputDecoration(labelText: "Discount (0-100)")
                ),
              ],
            ),
          ),
          actions: [
            TextButton(
              onPressed: () => Navigator.of(ctx).pop(),
              child: const Text("Cancel"),
            ),
            ElevatedButton(
              onPressed: (isEditing)
                  ? null
                  : () {
                if (_formKey.currentState!.validate()) {
                  _formKey.currentState!.save();
                  if (menuItem == null) {
                    _addMenuItem(
                      context,
                      double.parse(_priceController.text),
                      _ingredientsListController.text,
                      _photoController.text,
                      double.parse(_discountController.text),
                    );

                    _priceController.clear();
                    _ingredientsListController.clear();
                    _photoController.clear();
                    _discountController.clear();
                  } else {
                    _updateMenuItem(
                      context,
                      menuItem.id,
                      double.parse(_priceController.text),
                      _ingredientsListController.text,
                      _photoController.text,
                      double.parse(_discountController.text),
                    );
                  }
                  Navigator.of(ctx).pop();
                }
              },
              child: Text(menuItem == null ? "Add" : "Update"),
            ),
          ],
        ),
      ),
    );
  }

  Future<void> _addMenuItem(
      BuildContext context,
      double price,
      String ingredientsList,
      String photo,
      double discount,
      ) async {
    final provider = Provider.of<MenuItemProvider>(context, listen: false);
    final newMenuItem = MenuItem(
      id: 0, // DOESN'T MATTER
      idRestaurant: widget.restaurantId,
      name: _name!,
      price: price,
      ingredientsList: ingredientsList,
      photo: photo,
      discount: discount
    );
    final response = await provider.createMenuItem(newMenuItem);
    final responseJson = json.decode(response.body);
    if (responseJson.containsKey('id')) {
      final createdMenuItemId = responseJson['id'];
      newMenuItem.id = createdMenuItemId;
      log('MenuItem created with ID: $createdMenuItemId');
    } else {
      _showErrorDialog(context, 'Failed to create menu item.');
    }
  }

  Future<void> _updateMenuItem(
      BuildContext context,
      int id,
      double price,
      String ingredientsList,
      String photo,
      double discount,
      ) async {
    final provider = Provider.of<MenuItemProvider>(context, listen: false);
    final updatedMenuItem = MenuItem(
      id: id,
      idRestaurant: widget.restaurantId,
      name: _name!,
      price: price,
      ingredientsList: ingredientsList,
      photo: photo,
      discount: discount
    );
    await provider.updateMenuItem(id, updatedMenuItem);
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


  Future<void> _deleteMenuItem(BuildContext context, int id) async {
    final provider = Provider.of<MenuItemProvider>(context, listen: false);
    await provider.deleteMenuItem(id);
    setState(() {

    });
  }

  @override
  Widget build(BuildContext context) {
    final menuItemProvider = Provider.of<MenuItemProvider>(context);

    return Scaffold(
      appBar: AppBar(
        title: const Text("Manage Menu Items"),
        centerTitle: true,
      ),
      body: menuItemProvider.isLoading
          ? const Center(child: CircularProgressIndicator())
          : ListView.builder(
        itemCount: menuItemProvider.menuItems.length,
        itemBuilder: (ctx, index) {
          final menuItem = menuItemProvider.menuItems[index];
          return ListTile(
            title: Text(menuItem.name),
            trailing: Row(
              mainAxisSize: MainAxisSize.min,
              children: [
                IconButton(
                  icon: const Icon(Icons.edit),
                  onPressed: () => _showAddEditDialog(context, menuItem: menuItem),
                ),
                IconButton(
                  icon: const Icon(Icons.delete),
                  onPressed: () => _deleteMenuItem(context, menuItem.id),
                ),
              ],
            ),
          );
        },
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () => _showAddEditDialog(context),
        child: const Icon(Icons.add),
      ),
    );
  }
}
