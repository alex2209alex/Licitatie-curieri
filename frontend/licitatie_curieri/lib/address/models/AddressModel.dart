class Address {
  final int id;
  final String details;
  final double latitude;
  final double longitude;

  Address({
    required this.id,
    required this.details,
    required this.latitude,
    required this.longitude,
  });

  factory Address.fromJson(Map<String, dynamic> json) {
    return Address(
      id: json['id'],
      details: json['details'],
      latitude: json['latitude'],
      longitude: json['longitude'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'details': details,
      'latitude': latitude,
      'longitude': longitude,
    };
  }
}