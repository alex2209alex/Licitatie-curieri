class Restaurant {

  int id;
  String name;
  int addressId;




  Restaurant({
    required this.id,
    required this.name,
    required this.addressId,

});

  factory Restaurant.fromJson(Map<String, dynamic> json)
  {
    return Restaurant(
        id: json["id"],
        name: json["name"],
        addressId: json["address"].toDouble()
    );
  }

  Map<String, dynamic> toJson() {
    return {
      "id":id,
      "name":name,
      "address":addressId,
    };
  }



}