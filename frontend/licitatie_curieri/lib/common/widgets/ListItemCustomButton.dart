import 'package:flutter/material.dart';
class ListItemCustomButton extends StatelessWidget {
  final String label;
  final VoidCallback onPressed;


  const ListItemCustomButton({
    Key? key,
    required this.label,
    required this.onPressed
  }) : super(key: key);


  @override
  Widget build(BuildContext context) {
    return Container(
      width: 96,
      height: 32,
      child: FloatingActionButton(
        heroTag: UniqueKey(),
        backgroundColor: Colors.greenAccent,
        onPressed: onPressed,
        child: Center(
          child: Text(
            label,
            style: TextStyle(color: Colors.white),
          ),
        ),
      ),
    );
  }



}





