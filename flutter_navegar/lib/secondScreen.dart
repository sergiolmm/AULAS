
import 'package:flutter/material.dart';
import 'package:flutter_navegar/usuario.dart';

class secondScreen extends StatefulWidget {

  String str = "Inicial...";
  int   vInt ;

  secondScreen({this.str = "default", this.vInt: 0 });

  @override
  _secondScreenState createState() => _secondScreenState();
}



class _secondScreenState extends State<secondScreen> {
  static BuildContext ctx;
  usuario usu;
  @override
  Widget build(BuildContext context) {
    ctx = context;
    return Scaffold(
        appBar: AppBar(
          title: Text("Tela secundaria"),
          backgroundColor: Colors.blueAccent,
        ),
        body: Container(
          padding: EdgeInsets.all(30),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            children: <Widget>[
              RaisedButton(
                child: Text("Clique voltar"),
                padding: EdgeInsets.all(10),
                onPressed: () {
                  Navigator.pop(
                    context,
                  );
                },
              ),
              Text(
                "Valor passado : ${widget.str} - ${widget.vInt} "
              ),
              RaisedButton(
                child: Text("Clique voltar"),
                padding: EdgeInsets.all(10),
                onPressed: () {
                  Navigator.pop(
                      context, "Valor retornado é ... ${widget.vInt + 200}"
                  );
                },
              ),
              btn,
              BotaoParam(null,context),
              RaisedButton(
                child: Text("Clique atualizar classe"),
                padding: EdgeInsets.all(10),
                onPressed: () {
                  usu = usuario("Sergio L Moral Marques ", DateTime(2020,02,9));
                },
              ),
            ],
          ),
        )
    );
  }

  Widget BotaoParam(String valor, BuildContext ctx){
    if ((valor == null) | (valor == "")){
      valor = "sem valor";
    }
    Widget _btn = RaisedButton(
      child: Text(valor),
      padding: EdgeInsets.all(10),
      onPressed: () {
        //usuario usu = usuario("Sergio Moral  ", DateTime(2020,02,9));
        Navigator.pop(
            ctx,
            usu  // passando a classe como paramentro
        );
      },
    );
    return _btn;
  }

  Widget btn = RaisedButton(
              child: Text("Clique voltar Classe2"),
              padding: EdgeInsets.all(10),
              onPressed: () {
                usuario usu = usuario("Sergio M ", DateTime(2020,02,9));
                Navigator.pop(
                  ctx,
                  usu  // passando a classe como paramentro
                );
              },
          );
}
