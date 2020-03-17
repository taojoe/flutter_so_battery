import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:so_battery/so_battery.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  bool _hasPermission;
  bool _isIgnoringBatteryOptimizations;

// Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      platformVersion = await SoBattery.platformVersion;
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  void hasPermission() async{
    final hasPermission=await SoBattery.hasPermission();
    setState(() {
      _hasPermission=hasPermission;
    });
  }

  void isIgnoringBatteryOptimizations() async{
    final isIgnoringBatteryOptimizations= await SoBattery.isIgnoringBatteryOptimizations();
    setState(() {
      _isIgnoringBatteryOptimizations=isIgnoringBatteryOptimizations;
    });
  }

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Column(
          children: <Widget>[
            Center(
              child: Text('Running on: $_platformVersion\n'),
            ),
            Text('has Permission ${_hasPermission}'),
            RaisedButton(
              child: Text('has Permission'),
              onPressed: hasPermission
            ),
            Text('isIgnoringBatteryOptimizations ${_isIgnoringBatteryOptimizations}'),
            RaisedButton(
              child: Text('isIgnoringBatteryOptimizations'),
              onPressed: isIgnoringBatteryOptimizations
            ),
          ],
        ),
      ),
    );
  }
}
