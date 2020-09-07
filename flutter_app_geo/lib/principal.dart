import 'package:flutter/material.dart';
import 'package:location/location.dart';

    
class principal extends StatefulWidget {
  @override
  _principalState createState() => _principalState();
}

class _principalState extends State<principal> {

  Location location = new Location();
  Map<String, double> userLocation;

  bool _serviceEnabled;
  PermissionStatus _permissionGranted;
  LocationData _locationData;


  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Primeiro GPS"),
        backgroundColor: Colors.lightBlueAccent,
      ),
      body:Container(
        padding: EdgeInsets.all(20),
        child: Column(
          children: <Widget>[
            Text("Coordenadas GPS"),

          ],
        ),
      )

    );
  }
  Future<Map<String, double>> _getLocation() async{
    var currentLocation = <String, double>{};
    try{
      _locationData = await location.getLocation();

    } catch(e){
      currentLocation = null;
    }
    return currentLocation;
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
}
    