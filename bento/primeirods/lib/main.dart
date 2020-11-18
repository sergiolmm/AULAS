import 'package:flutter/material.dart';

void main() {
  runApp(MaterialApp(
    debugShowCheckedModeBanner: true,
    title: "Primeiro",
    home: Principal(),
  ));
}

class Principal extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Container(
      child: Column(
        children: [
          Text("Alo"),
          Text("Mundo"),
          Text("Deu certo"),
          Row(children: [
            Text("R 1"),
            Text("R 2"),
            Text("R 3"),
          ])
        ],
      ),
    );
  }
}
