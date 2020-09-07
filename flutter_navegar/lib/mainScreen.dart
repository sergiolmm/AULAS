import 'package:flutter/material.dart';
import 'package:flutter_navegar/secondScreen.dart';

class mainScreen extends StatefulWidget {
  @override
  _mainScreenState createState() => _mainScreenState();
}

class _mainScreenState extends State<mainScreen> {
  String txt = "alo";

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Tela principal"),
        backgroundColor: Colors.blueAccent,
      ),
      body: Container(
        padding: EdgeInsets.all(30),
        child: Column(
          mainAxisAlignment : MainAxisAlignment.spaceEvenly,
          children: <Widget>[
            RaisedButton(
              child: Text("Clique"),
              padding: EdgeInsets.all(10),
              onPressed: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (context) => secondScreen()
                  )
                );
              },
            ),
            RaisedButton(
              child: Text("Clique 2"),
              padding: EdgeInsets.all(10),
              onPressed: () {
                Navigator.push(
                    context,
                    MaterialPageRoute(
                        builder: (context) => secondScreen( str: "Frase Nova"),
                    )
                );
              },
            ),
            RaisedButton(
              child: Text("Clique 3"),
              padding: EdgeInsets.all(10),
              onPressed: () {
                Navigator.push(
                    context,
                    MaterialPageRoute(
                      builder: (context) => secondScreen( vInt: 100,),
                    )
                );
              },
            ),
            Text(txt),
            RaisedButton(
              child: Text("Clique 4"),
              padding: EdgeInsets.all(10),
              onPressed: () {
                _botaoRetorno(context);
              }
            ),
            RaisedButton(
                child: Text("Clique Classe"),
                padding: EdgeInsets.all(10),
                onPressed: () {
                  _botaoRetornoClasse(context);
                }
            ),
          ],
        ),
      )
    );
  }

  void _botaoRetorno(BuildContext context) async{
    await Navigator.push(
      context,
      MaterialPageRoute(
        builder: (context) => secondScreen(vInt: 200,),
      ),
    ).then((value) {
      if (value != null){
        print(value);
        setState((){
          txt = value;
        });
      }
    });
  }

  void _botaoRetornoClasse(BuildContext context) async{
    await Navigator.push(
      context,
      MaterialPageRoute(
        builder: (context) => secondScreen(),
      ),
    ).then((value) {
      if (value != null){
        setState((){
          txt = value.getNome + '\n'+ value.getTudo() +'\n' + value.getEmail;
        });
      }
    });
  }


}
