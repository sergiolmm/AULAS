import 'package:flutter/material.dart';
import 'package:flutter_navegacao/Classe1.dart';
import 'package:flutter_navegacao/SecondScreen.dart';

class MainScreen extends StatefulWidget {
  @override
  _MainScreenState createState() => _MainScreenState();
}

String txt = "Alo mundo";
String txt2 = "Alo mundo";

class _MainScreenState extends State<MainScreen> {

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Tela principal"),
        backgroundColor: Colors.amber,
      ),
      body: Container(
        padding: EdgeInsets.all(30),
        child: Column(
          children: <Widget>[
            RaisedButton(
              child: Text("ir para proxima tela"),
              padding: EdgeInsets.all(10),
              onPressed: () {
                Navigator.push(
                    context,
                    MaterialPageRoute(
                        builder: (context) => SecondScreen(valor: "Sergio",)
                    )
                );
              },
            ),
            RaisedButton(
              child: Text("ir para proxima tela 2"),
              padding: EdgeInsets.all(10),
              onPressed: () {
                Navigator.push(
                    context,
                    MaterialPageRoute(
                        builder: (context) => SecondScreen(vInt: 200,)
                    )
                );
              },
            ),
            RaisedButton(
              child: Text("Espera retorno"),
              padding: EdgeInsets.all(10),
              onPressed: () {
                _botaoRetorno(context);
              },
            ),

            Text(txt),
            RaisedButton(
              child: Text("Espera retorno33"),
              padding: EdgeInsets.all(10),
              onPressed: () {
                _botaoRetorno2(context);
              },
            ),
          ],
        ),
      ),
    );
  }

  void _botaoRetorno(BuildContext context) async {
    await Navigator.push(
      context,
      MaterialPageRoute(
        builder: (context) => SecondScreen(vInt: 200,),
      ),
    ).then((val) {
      if (val != null) {
        print(val);
        setState(() {
          txt = val;
        });
      }
    });
  }

  void _botaoRetorno2(BuildContext context) async {
    await Navigator.push(
      context,
      MaterialPageRoute(
        builder: (context) => SecondScreen(vInt: 200,),
      ),
    ).then((value) {
      if (value != null) {
        print(value);
        setState(() {
          txt = value.getNome + '\n' + value.getTudo();

        });
      }
    });
  }

}