import 'package:flutter/material.dart';
import 'package:location/location.dart';

class Principal extends StatefulWidget {
  @override
  _PrincipalState createState() => _PrincipalState();
}
String str = "Sem valor";
String str2 = "Sem valor";


class _PrincipalState extends State<Principal> {


  var location = new Location();

  bool _serviceEnabled;
  PermissionStatus _permissionGranted;
  LocationData _locationData;
  LocationData _locationData2;

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    location.onLocationChanged.listen((LocationData currentLocation) {
      setState(() {
        str2 = currentLocation.toString();
        print("localização");
      });// Use current location
    });
  }

  void servico() async{
    _serviceEnabled = await location.serviceEnabled();
    if (!_serviceEnabled) {
      _serviceEnabled = await location.requestService();
      if (!_serviceEnabled) {
        return;
      }
    }
  }
  void permissao() async{
    _permissionGranted = await location.hasPermission();
    if (_permissionGranted == PermissionStatus.denied) {
      _permissionGranted = await location.requestPermission();
      if (_permissionGranted != PermissionStatus.granted) {
        return;
      }
    }
  }
  Future _getLocation() async{
    _locationData = await location.getLocation();
    return _locationData;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Geo 2"),
        backgroundColor: Colors.lightBlueAccent,
      ),
      body: Container(
        padding: EdgeInsets.all(30),
        child: Column(
          children: <Widget>[
            Text("Primeiro teste do GPS"),
            RaisedButton(
              child: Text("GPS "),
              padding: EdgeInsets.all(10),
              onPressed: () {
                servico();
                if (_permissionGranted == PermissionStatus.denied){
                  permissao();
                } else {
                  _getLocation().then((value){
                     setState(() {
                       str = _locationData.toString();
                    });
                  });

                }


              },
            ),
            Text(str),
            Text(str2),
          ],
        ),
      ),
    );
  }
}
