class MenuItem{

  int id;
  String name;
  double price;
  String ingredientsList;
  String photo;
  double discount;



  MenuItem({
   required this.id,
   required this.name,
   required this.price,
   required this.ingredientsList,
   required this.photo,
   required this.discount
});


  factory MenuItem.fromJson(Map<String, dynamic> json) {
    return MenuItem(
        id: json["id"],
        name: json["name"],
        price: json["price"].toDouble(),
        ingredientsList: json["ingredientsList"],
        photo: json["photo"],
        discount: json["discount"].toDouble());

  }

  Map<String, dynamic> toJson(){
    return {
      "id":id,
      "name":name,
      "price":price,
      "ingredientsList":ingredientsList,
      "photo":photo,
      "discount":discount
    };

  }

  double getFinalPrice() {return (100-discount)/100*price; }


}