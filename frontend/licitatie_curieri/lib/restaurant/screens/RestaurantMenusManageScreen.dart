import 'dart:developer';

import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../../common/widgets/LogoutActionBarButton.dart';
import '../models/MenuItemModel.dart';
import '../providers/MenuItemProvider.dart';

class RestaurantMenusManageScreen extends StatefulWidget {
  final int restaurantId;
  final String restaurantName;

  const RestaurantMenusManageScreen(
      {Key? key, required this.restaurantId, required this.restaurantName})
      : super(key: key);

  @override
  State<RestaurantMenusManageScreen> createState() =>
      _RestaurantMenusManageScreenState();
}

class _RestaurantMenusManageScreenState
    extends State<RestaurantMenusManageScreen> {
  final _formKey = GlobalKey<FormState>();
  String? _itemName;
  double? _itemPrice;
  String? _ingredientsList;
  String? _photo;
  double? _discount;

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      Provider.of<MenuItemProvider>(context, listen: false)
          .fetchMenuItems(widget.restaurantId);
      setState(() {

      });
    });
    log("MenuItem Manage Screen for Restaurant ${widget.restaurantName} id ${widget.restaurantId}");
    log("MenuItems number ${Provider.of<MenuItemProvider>(context, listen: false).menuItems.length}");
  }

  void _showAddEditDialog(BuildContext context, {MenuItem? menuItem}) {
    if (menuItem != null) {
      _itemName = menuItem.name;
      _itemPrice = menuItem.price;
      _ingredientsList = menuItem.ingredientsList;
      _photo = menuItem.photo;
      _discount = menuItem.discount;
    } else {
      _itemName = null;
      _itemPrice = null;
      _ingredientsList = null;
      _photo = null;
      _discount = null;
    }

    showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        title: Text(menuItem == null ? "Add Menu Item" : "Edit Menu Item"),
        content: Form(
          key: _formKey,
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              TextFormField(
                initialValue: _itemName,
                decoration: const InputDecoration(labelText: "Item Name"),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return "Please enter an item name.";
                  }
                  return null;
                },
                onSaved: (value) => _itemName = value,
              ),
              TextFormField(
                initialValue: _itemPrice?.toString(),
                decoration: const InputDecoration(labelText: "Price"),
                keyboardType: TextInputType.number,
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return "Please enter a price.";
                  }
                  if (double.tryParse(value) == null) {
                    return "Please enter a number.";
                  }
                  if(double.parse(value) < 0.00)
                    {
                      return "Please enter a valid number.";
                    }
                  return null;
                },
                onSaved: (value) => _itemPrice = double.parse(value!),
              ),
              TextFormField(
                initialValue: _ingredientsList?.toString(),
                decoration: const InputDecoration(labelText: "Ingredients"),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return "Please enter a list of ingredients.";
                  }
                  return null;
                },
                onSaved: (value) => _ingredientsList = value,
              ),
              TextFormField(
                initialValue: _photo?.toString(),
                decoration: const InputDecoration(labelText: "Photo"),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return "Please enter a photo path.";
                  }
                  return null;
                },
                onSaved: (value) => _photo = value,
              ),
              TextFormField(
                initialValue: _discount?.toString(),
                decoration: const InputDecoration(labelText: "Discount"),
                keyboardType: TextInputType.number,
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return "Please enter a discount.";
                  }
                  if (double.tryParse(value) == null) {
                    return "Please enter number.";
                  }
                  if(double.parse(value) < 0.00 || double.parse(value) >100.00)
                    {
                      return "Please enter a valid number.";
                    }
                  return null;
                },
                onSaved: (value) => _discount = double.parse(value!),
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
            onPressed: () {
              if (_formKey.currentState!.validate()) {
                _formKey.currentState!.save();
                if (menuItem == null) {
                  _addMenuItem(context);
                } else {
                  _updateMenuItem(context, menuItem.id);
                }
                Navigator.of(ctx).pop();
              }
            },
            child: Text(menuItem == null ? "Add" : "Update"),
          ),
        ],
      ),
    );
  }

  Future<void> _addMenuItem(BuildContext context) async {
    final provider = Provider.of<MenuItemProvider>(context, listen: false);
    final newItem = MenuItem(
      id: 0,
      name: _itemName!,
      price: _itemPrice!,
      ingredientsList: _ingredientsList!,
      photo: _photo!,
      discount: _discount!,
      restaurantId: widget.restaurantId,
    );
    await provider.createMenuItem(newItem);
    await provider.fetchMenuItems(widget.restaurantId);
  }

  Future<void> _updateMenuItem(BuildContext context, int id) async {
    final provider = Provider.of<MenuItemProvider>(context, listen: false);
    final updatedItem = MenuItem(
      id: id,
      name: _itemName!,
      price: _itemPrice!,
      ingredientsList: _ingredientsList!,
      photo: _photo!,
      discount: _discount!,
      restaurantId: widget.restaurantId,
    );
    await provider.updateMenuItem(id, updatedItem);
    await provider.fetchMenuItems(widget.restaurantId);
  }

  Future<void> _removeMenuItem(BuildContext context, int id) async {
    final provider = Provider.of<MenuItemProvider>(context, listen: false);
    await provider.removeMenuItem(id);
    await provider.fetchMenuItems(widget.restaurantId);
  }

  @override
  Widget build(BuildContext context) {
    final menuItemProvider = Provider.of<MenuItemProvider>(context);
    return Scaffold(
      appBar: AppBar(
        title: Text("Menus for ${widget.restaurantName}"),
        centerTitle: true,

        actions: [
          LogoutActionBarButton(),
        ]
      ),
      body: menuItemProvider.isLoading
          ? const Center(child: CircularProgressIndicator())
          : ListView.builder(
              itemCount: menuItemProvider.menuItems.length,
              itemBuilder: (ctx, index) {
                final menuItem = menuItemProvider.menuItems[index];
                menuItem.restaurantId = widget.restaurantId;
                return ListTile(
                  title: Text(menuItem.name),
                  subtitle:
                      menuItem.discount == 0 ?
                      Text("Price: \$${menuItem.price.toStringAsFixed(2)}")
                  :Row(
                        children: [
                          Text(
                            "Price: \$${menuItem.price.toStringAsFixed(2)}",
                            style: const TextStyle(
                              decoration: TextDecoration.lineThrough,
                              color: Colors.grey,
                            ),
                          ),
                          SizedBox(width: 8),
                          Text(
                            "\$${menuItem.getFinalPrice().toStringAsFixed(2)}",
                            style: const TextStyle(
                              fontWeight: FontWeight.bold,
                              color: Colors.red,
                            ),
                          ),
                        ],
                      ),
                  trailing: Row(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      IconButton(
                        icon: const Icon(Icons.edit),
                        onPressed: () =>
                            _showAddEditDialog(context, menuItem: menuItem),
                      ),
                      IconButton(
                        icon: const Icon(Icons.delete),
                        onPressed: () => _removeMenuItem(context, menuItem.id),
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
