class MenuItem{

  int id;
  int idRestaurant;
  String name;
  double price;
  String ingredientsList;
  String photo;
  double discount;



  MenuItem({
   required this.id,
   required this.idRestaurant,
   required this.name,
   required this.price,
   required this.ingredientsList,
   required this.photo,
   required this.discount
});


  factory MenuItem.fromJson(Map<String, dynamic> json) {
    return MenuItem(
        id: json["id"],
        idRestaurant: json["idRestaurant"] ?? 0,  // To do: remove after adding "idRestaurant" column in database TO-DO: WIP: W.I.P.
        name: json["name"],
        price: json["price"].toDouble(),
        ingredientsList: json["ingredientsList"],
        photo: json["photo"],
        discount: json["discount"].toDouble());

  }

  Map<String, dynamic> toJson(){
    return {
      "id":id,
      "idRestaurant":idRestaurant,
      "name":name,
      "price":price,
      "ingredientsList":ingredientsList,
      "photo":photo,
      "discount":discount
    };

  }

  double getFinalPrice() {return (100-discount)/100*price; }


}