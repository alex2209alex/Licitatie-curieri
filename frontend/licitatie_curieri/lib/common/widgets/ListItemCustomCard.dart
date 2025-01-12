import 'package:flutter/material.dart';
import 'package:licitatie_curieri/restaurant/models/MenuItemModel.dart';
import 'package:licitatie_curieri/restaurant/models/RestaurantModel.dart';
import '../../restaurant/models/OrderModel.dart';
import 'ListItemCustomButton.dart';

class ListItemCustomCard extends StatelessWidget {
  final String title;
  final String description;
  final String imageUrl;
  final String buttonText;
  final VoidCallback onButtonPressed;

  ListItemCustomCard({
    Key? key,
    required this.title,
    required this.description,
    required this.imageUrl,
    required this.buttonText,
    required this.onButtonPressed,
  }) : super(key: key);

  factory ListItemCustomCard.fromMenuItem(MenuItem menuItem, String buttonText, VoidCallback onButtonPressed)
  {
    return ListItemCustomCard(
        title: menuItem.name,
        description: menuItem.getFinalPrice().toString() + " lei\n" + menuItem.ingredientsList,
        imageUrl: menuItem.photo,
        buttonText: buttonText,
        onButtonPressed: onButtonPressed
    );
  }

  factory ListItemCustomCard.fromRestaurant(Restaurant restaurant, String buttonText, VoidCallback onButtonPressed)
  {
    return ListItemCustomCard(
        title: restaurant.name,
        description: restaurant.address!.details.toString(),
        imageUrl: "https://www.firstbenefits.org/wp-content/uploads/2017/10/placeholder.png",
        buttonText: buttonText,
        onButtonPressed: onButtonPressed
    );
  }

  factory ListItemCustomCard.fromOrder(Order order, String buttonText, VoidCallback onButtonPressed)
  {
    return ListItemCustomCard(
        title: "Comanda la: ${order.restaurantName}",
        description: "Stare: ${order.orderStatus}",
        imageUrl: "https://www.firstbenefits.org/wp-content/uploads/2017/10/placeholder.png",
        buttonText: buttonText,
        onButtonPressed: onButtonPressed
    );
  }

  @override
  Widget build(BuildContext context) {
    return Card(
      child: Stack(
        children: [
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: Row(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Image(
                  width: 128,
                  height: 128,
                  image: NetworkImage(imageUrl),
                  fit: BoxFit.cover,
                ),

                // Text: Title + Description
                Expanded(
                  child: Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          title,
                          style: const TextStyle(fontWeight: FontWeight.bold),
                          maxLines: 1,
                          overflow: TextOverflow.ellipsis,
                        ),
                        const SizedBox(height: 4),
                        Text(
                          description,
                          maxLines: 2,
                          overflow: TextOverflow.ellipsis,
                        ),
                      ],
                    ),
                  ),
                ),
              ],
            ),
          ),

          // Custom Button
          Positioned(
            bottom: 8.0,
            right: 8.0,
            child: ListItemCustomButton(
              label: buttonText,
              onPressed: onButtonPressed,
            ),
          ),
        ],
      ),
    );
  }
}
