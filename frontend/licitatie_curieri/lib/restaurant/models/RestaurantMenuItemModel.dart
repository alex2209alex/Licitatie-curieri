
import 'MenuItemModel.dart';

class RestaurantMenuItem{

  int restaurantId;
  MenuItem menuItem;

  RestaurantMenuItem({
    required this.restaurantId,
    required this.menuItem
  });

  factory RestaurantMenuItem.fromJson(Map<String, dynamic> json) {
    return RestaurantMenuItem(
      restaurantId: json['restaurantId'],
      menuItem: MenuItem.fromJson(json['menuItem'])
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'restaurantId': restaurantId,
      'menuItem': menuItem.toJson()
    };
  }
}