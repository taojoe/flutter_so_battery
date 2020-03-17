import 'dart:async';

import 'package:flutter/services.dart';

class SoBattery {
  static const MethodChannel _channel = const MethodChannel('so_battery/method');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
  static Future<bool> hasPermission() async {
    final bool version = await _channel.invokeMethod('hasPermission');
    return version;
  }
  static Future<bool> isIgnoringBatteryOptimizations() async {
    final bool version = await _channel.invokeMethod('isIgnoringBatteryOptimizations');
    return version;
  }
}
