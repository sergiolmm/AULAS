import 'package:flutter/material.dart';
import 'package:location/location.dart';

class principal extends StatefulWidget {
  @override
  _principalState createState() => _principalState();
}

String strLocalizacao = "Sem valor";
String strLocalizacao2 = "Sem valor";

class _principalState extends State<principal> {

  bool _serviceEnabled;
  PermissionStatus _permissionGranted;

  var location = new Location();

  LocationData _locationData;
  LocationData _locationData2;

  void servico() async  {
    _serviceEnabled = await location.serviceEnabled();
    if (!_serviceEnabled){
      _serviceEnabled = await location.requestService();
      if (!_serviceEnabled)
        return;
    }
  }
  void setPermission() async {
    _permissionGranted = await location.hasPermission();
    if (_permissionGranted == PermissionStatus.denied) {
      _permissionGranted = await location.requestPermission();
      if (_permissionGranted == PermissionStatus.denied) {
        return;
      }
    }
  }

  Future _getLocation() async{
    _locationData = await location.getLocation();
    return _locationData;

  }

  @override
  void initState(){
    super.initState();
    location.onLocationChanged.listen((LocationData currentLocation) {
      setState( () {
        strLocalizacao2 = currentLocation.latitude.toString() + "," +
                          currentLocation.longitude.toString();
      });
    });
  }


  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar:  AppBar(
        title: Text("Localização"),
        backgroundColor: Colors.black26,
      ),
      body: Container(
        padding: EdgeInsets.all(20),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.center,
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: <Widget>[
            Text("Serviço de localização", style: TextStyle(fontSize: 20),),
            Text(strLocalizacao, style: TextStyle(fontSize: 20)),
            RaisedButton(
              child: Text("GPS"),
              padding: EdgeInsets.all(10),
              onPressed: () {
                servico();
                if (_permissionGranted == PermissionStatus.denied) {
                  setPermission();
                }
                else {
                  _getLocation().then((value){
                    setState(() {
                     LocationData local = value;
                     strLocalizacao = local.longitude.toString() + " , " +
                                      local.latitude.toString();
                    });
                  });
                }

              },
            ),
            Text(strLocalizacao2, style: TextStyle(fontSize: 18),)
          ],
        ),
      ),
    );
  }
}
