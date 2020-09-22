import 'package:flutter/material.dart';



void main() {
  runApp(MaterialApp(
    debugShowCheckedModeBanner: false,
    home: home2(), // app
  ));
}

class home2 extends StatefulWidget {
  @override
  _home2State createState() => _home2State();
}

class _home2State extends State<home2> {
  var contador = 0;
  var frase1 = "alo mundo.. .";
  var soma = "somar";
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.greenAccent,
        title: Text("titulo Statefull"),
      ),
      body: Padding(
        padding: EdgeInsets.all(30),
        child: Column(
          children: <Widget>[
            Text(frase1),
            textoTitulo,
            Text("4 info not rule´s",
              style: estilo1,
            ),
            FlatButton(
              onPressed: (){
                print("clicou em mim");
                setState(() {
                  contador++;
                  frase1 = "voce clicou em mim " + '$contador';

                });
              },
              child:
                Text("click me"),
            ),
            Text("------"),
            RaisedButton(
              onPressed: (){
                setState(() {
                  frase1 = "limpou";
                });
              },
              child: Text("Limpar"),
            )
          ],
        ),
      ),
      bottomNavigationBar: BottomAppBar(
        color: Colors.deepPurple,
        child: Padding(
          padding: EdgeInsets.all(20),
          child: Row(
            children: <Widget>[
              Text("t1"),
              Text("t2"),
              FlatButton(
                onPressed: (){
                  setState(() {
                    _tit = "Valeu"; // não vai funcionar pois nao reconstroi o
                                    // textoTitulo
                  });
                },
                child: Text("alo"),
              )
            ],
          ),
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: funtion1,
        backgroundColor: Colors.amberAccent,
        tooltip: soma,
        child: Icon(Icons.vibration),
      ),
    );
  }
  void funtion1() {
    setState(() {
       soma = "teste";
    });
  }
}





/*
*   classe stateless
*
* */

class home1 extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.greenAccent,
        title: Text("titulo .."),
      ),
      body: Padding(
        padding: EdgeInsets.all(30),
        child: Column(
          children: <Widget>[
            Text("Alo Mundo"),

            Text("4 info not rule´s",
              style: estilo1,
              ),
          ],
        ),
      ),
      bottomNavigationBar: BottomAppBar(
        color: Colors.deepPurple,
        child: Padding(
          padding: EdgeInsets.all(20),
          child: Row(
            children: <Widget>[
              Text("t1"),
              Text("t2"),
            ],
          ),
        ),
      ),
    );
  }
}
var _tit = "titulo";

Text textoTitulo = Text(
  _tit,
  style: estilo1,
);

TextStyle estilo1 = TextStyle(
                        fontSize: 25,
                        backgroundColor: Colors.blueGrey,
                        );
/**
 *    class x {
 *       construtor ( p1, p2,p3,...);
 *    }
 *
 *    x( p1 ( p2 ( p3(dd) ) ) );
 *
 *
 *
 *
 */

