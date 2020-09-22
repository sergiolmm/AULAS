
import 'package:flutter/material.dart';
import 'package:flutter_navegacao/Classe1.dart';


class SecondScreen extends StatefulWidget {

  String valor = "ddd";
  int    vInt;

  //SecondScreen(this.valor);  // o valor é obrigatorio
  SecondScreen({this.valor = "", this.vInt:0});  // o valor é opcional

  @override
  _SecondScreenState createState() => _SecondScreenState();
}

class _SecondScreenState extends State<SecondScreen> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Tela principal"),
        backgroundColor: Colors.blue,
      ) ,
      body: Container(
        padding: EdgeInsets.all(30),
        child: Column(
          children: <Widget>[
            Text("Essa é a nova tela ->  ${widget.valor} -- ${widget.vInt}"),
            RaisedButton(
              child: Text("Voltar valor "),
              padding: EdgeInsets.all(10),
              onPressed: () {
                Navigator.pop(
                    context,
                    "texto Retornado... ${widget.vInt}"
                    );
              },
            ),
            RaisedButton(
              child: Text("Voltar valor "),
              padding: EdgeInsets.all(10),
              onPressed: () {
                Classe1 cla = Classe1("teste", DateTime(2001,11,6));
                Navigator.pop(
                    context,
                    cla
                );
              },
            )
          ],
        ),
      ),
    );
  }
}
