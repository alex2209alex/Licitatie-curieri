import 'package:flutter/cupertino.dart';
import 'package:licitatie_curieri/restaurant/services/MenuItemService.dart';

import '../models/MenuItemModel.dart';

class MenuItemProvider with ChangeNotifier {

  List<MenuItem> _menuItems = [];
  MenuItem? _selectedMenuItem;
  bool _isLoading = false;

  List<MenuItem> get MenuItems => _menuItems;
  MenuItem? get selectedMenuItem => _selectedMenuItem;
  bool get isLoading => _isLoading;

  Future<void> fetchMenuItems(int restaurantId) async {
    _isLoading = true;
    notifyListeners();

    try{
      _menuItems = await MenuItemService().fetchMenuItemsByRestaurant(restaurantId);
    }catch(error)
    {
      print("Error fetching menuItems from Restaurant $restaurantId: $error");
    } finally{
      
      
      _isLoading = false;
      initWithoutBackEnd(restaurantId);
      notifyListeners();
    }
  }

  void initWithoutBackEnd(int restaurantId)
  {
    _menuItems = [];
    switch(restaurantId)
        {
      case 1:
          final menu = MenuItem(id: 1, name: "item1", price: 12, ingredientsList: "lista1", photo: "https://cvmi.cname.ro/850_530/produsele-fast-food-mai-periculoase-decat-se-credea/article/mediaPool/cheeseburger-2015-09.jpg", discount: 0);
          _menuItems.add(menu);
          break;
      case 2:
        final menu1 = MenuItem(id: 2, name: "item2", price: 1, ingredientsList: "lista2", photo: "https://media.istockphoto.com/id/908663850/ro/fotografie/diverse-produse-fast-food.jpg?s=612x612&w=0&k=20&c=6RvI38yNXETtW1_UrzFOllLpe6PIurUxU2SY6BuQqtE=", discount: 0);
        final menu2 = MenuItem(id: 3, name: "item3", price: 65, ingredientsList: "lista3", photo: "https://www.catena.ro/assets/uploads/files/images/Lista-de-ingrediente-periculoase-din-alimentele-fast-food.jpg", discount: 0);
        _menuItems.add(menu1);
        _menuItems.add(menu2);
        break;
      default:
        break;
    }

  }
  Future<void> fetchMenuItemById(int id) async {
    _isLoading = true;
    notifyListeners();

    try {
      _selectedMenuItem = await MenuItemService().fetchMenuItemById(id);
    } catch(error)
    {
      print("Error fetching menuItem $id: $error");
    } finally {
      _isLoading = false;
      notifyListeners();
    }

  }

}