import 'package:flutter/material.dart';
import 'package:http/http.dart' as meuHttp;
import 'dart:convert';

class principal extends StatefulWidget {
  @override
  _principalState createState() => _principalState();
}

class _principalState extends State<principal> {
  String  strEndereco = "";

  _getCEP() async {
    String url = "https://viacep.com.br/ws/13054607/json/";

    meuHttp.Response response;

    response = await meuHttp.get(url);
    Map<String, dynamic> retorno = json.decode(response.body);
    String cep = retorno["cep"];
    String logradouro = retorno["logradouro"];
    String ret = "Cep = ${cep} - Rua : ${logradouro}";

    setState(() {
      strEndereco = ret;
    });
  }

  Future _getCEP2() async {
    String url = "https://viacep.com.br/ws/13100101/json/";

    meuHttp.Response response;

    response = await meuHttp.get(url);
    Map<String, dynamic> retorno = json.decode(response.body);
    String cep = retorno["cep"];
    String logradouro = retorno["logradouro"];
    String ret = "Cep = ${cep} - Rua : ${logradouro}";

    return ret;
  }


  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Consumindo serviços"),
        backgroundColor: Colors.lightGreenAccent,
      ),
      body: Container(
        padding: EdgeInsets.all(30),
        child: Column(
          children: <Widget>[
            Text(strEndereco),
            RaisedButton(
              child: Text("consumir"),
              onPressed: (() {
               // _getCEP();
                _getCEP2().then((value) {
                  setState(() {
                    strEndereco = value;
                  });
                });
              }) ,
            )
          ],
        ),
      ),
    );
  }




}
