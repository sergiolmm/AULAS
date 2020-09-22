import 'package:flutter/material.dart';

class MainScreen extends StatefulWidget {
  @override
  _State createState() => _State();
}

class _State extends State<MainScreen> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: Text("Bento"),
          backgroundColor: Colors.amber[200],
        ),
        body: Container(
          padding: EdgeInsets.all(10),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.center,
            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            children: <Widget>[
              Text(
                "Alo",
                textAlign: TextAlign.center,
              ),
              Text("Tudo Bem"),
              RaisedButton(
                  child: Text("clique aqui"),
                  padding: EdgeInsets.all(5),
                  onPressed: () {
                    print("Voce clicou no botao");
                  }),
              Text("Tudo Bem"),
            ],
          ),
        ));
  }
}
