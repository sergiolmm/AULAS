import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class Home extends StatefulWidget {
  @override
  _HomeState createState() => _HomeState();
}

class _HomeState extends State<Home> {

  var imagem = AssetImage("images/mira.png");
  var homer  = AssetImage("images/883238845.png");
  var cnt = 0;
  var frase1 = "alo ....3 ";

  TextEditingController _textController = TextEditingController();

  void click(String nome, int valor){
    print(nome);

    if (valor == 3)
      setState(() {
        this.frase1 = _textController.text + '---';
      });

    switch(cnt){
      case 0:
        setState(() {
          this.homer = AssetImage("images/logoCotuca.png");
        });
        break;
      case 1:
        setState(() {
          this.homer = AssetImage("images/aviao1.png");
        });
        break;
    }
    cnt++;
    if (cnt > 1)
      cnt = 0;
  }


  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Projeto de imagem e texto"),
        backgroundColor: Colors.lightBlue,
      ),
      body: Padding(
        padding: EdgeInsets.all(10),
        child: Container(
            padding: EdgeInsets.all(10),
            decoration: BoxDecoration(
                border: Border.all(
                  color: Colors.amber,
                  width: 4,
                )
            ),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.center,
              //mainAxisAlignment:MainAxisAlignment.spaceBetween ,
              children: <Widget>[
                Text(frase1),
                Text("alo....12334444556563"),
                Image(image: homer, width: 50),
                GestureDetector(
                  onDoubleTap: (){
                    debugPrint("Clicou");
                  },
                  onTap: () => click("Tap...", 0),
                  child: Padding(
                      padding: EdgeInsets.all(10),
                      child: Image(image: imagem,)
                  ),
                ),
                TextField(
                  keyboardType: TextInputType.number,
                  decoration: InputDecoration(
                    labelText: "Escolha a imagem?"
                  ),
                  enabled: true,
                  maxLength: 2,
                  maxLengthEnforced: true,
                  style: TextStyle(
                    fontSize: 20,
                    color: Colors.green
                  ),
                  obscureText: false,
                  onChanged: (String texto){
                    print("Valor digitado = "+ texto);
                    click("", 3);
                  },
                  onSubmitted:(String texto){
                    print("Valor submetido = "+ texto);
                     setState(() {
                       frase1 = texto;
                     });
                  },
                  controller: _textController,
                ),
                Row(
                  mainAxisAlignment:MainAxisAlignment.spaceEvenly ,
                  children: <Widget>[
                    RaisedButton(
                      onPressed: (){},
                      child: Text("B1"),
                    ),
                    RaisedButton(
                      onPressed: (){},
                      child: Text("B2"),
                    ),
                    RaisedButton(
                      onPressed: (){},
                      child: Text("B3"),
                    )
                  ],
                )
              ],
            )
        )
        ,
      )
    );
  }
}
