class Restaurant {

  int id;
  String name;
  String photo;
  String address;
  double latitude;
  double longitude;



  Restaurant({
    required this.id,
    required this.name,
    required this.photo,
    required this.address,
    required this.latitude,
    required this.longitude
});

  factory Restaurant.fromJson(Map<String, dynamic> json)
  {
    return Restaurant(
        id: json["id"],
        photo: json["photo"],
        name: json["name"],
        address: json["latitude"].toDouble(),
        latitude: json["longitude"].toDouble(),
        longitude: json["longitude"].toDouble());
  }

  Map<String, dynamic> toJson() {
    return {
      "id":id,
      "name":name,
      "photo":photo,
      "address":address,
      "latitude":latitude,
      "longitude":longitude
    };
  }



}