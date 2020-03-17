package com.github.taojoe.so_battery

import android.content.Context
import android.content.pm.PackageManager
import androidx.annotation.NonNull;
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry
import io.flutter.plugin.common.PluginRegistry.Registrar

enum class PermissionResult{
  GRANTED, PERMISSION_DENIED, PERMISSION_DENIED_NEVER_ASK
}
/** SoBatteryPlugin */
public class SoBatteryPlugin: FlutterPlugin, MethodCallHandler, ActivityAware, PluginRegistry.RequestPermissionsResultListener {
  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    init(flutterPluginBinding.binaryMessenger, flutterPluginBinding.applicationContext)
  }

  // This static function is optional and equivalent to onAttachedToEngine. It supports the old
  // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
  // plugin registration via this function while apps migrate to use the new Android APIs
  // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
  //
  // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
  // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
  // depending on the user's project. onAttachedToEngine or registerWith must both be defined
  // in the same class.
  companion object {
    val METHOD_CHANNEL_NAME="so_battery/method"
    val PERMISSION_REQUEST_CODE="so_battery".hashCode()
    lateinit var instance:SoBatteryPlugin
    @JvmStatic
    fun registerWith(registrar: Registrar) {
      init(registrar.messenger(), registrar.context())
      registrar.addRequestPermissionsResultListener(instance)
    }
    fun init(messenger: BinaryMessenger, context: Context){
      instance= SoBatteryPlugin()
      val channel = MethodChannel(messenger, METHOD_CHANNEL_NAME)
      channel.setMethodCallHandler(instance)
    }
  }

  private var activityBinding:ActivityPluginBinding?=null
  private var currentResult: Result?=null

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    if (call.method == "getPlatformVersion") {
      result.success("Android ${android.os.Build.VERSION.RELEASE}")
    } else {
      result.notImplemented()
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
  }

  override fun onDetachedFromActivity() {
    activityBinding?.removeRequestPermissionsResultListener(this)
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    binding.addRequestPermissionsResultListener(this)
    activityBinding=binding
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    onReattachedToActivityForConfigChanges(binding)
  }

  override fun onDetachedFromActivityForConfigChanges() {
    onDetachedFromActivity()
  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>?, grantResults: IntArray?): Boolean {
    if(requestCode == PERMISSION_REQUEST_CODE){
      if(grantResults?.firstOrNull()== PackageManager.PERMISSION_GRANTED){
        currentResult?.success(PermissionResult.GRANTED.name)
      }else{
        currentResult?.success(PermissionResult.PERMISSION_DENIED.name)
      }
      currentResult=null
      return true
    }
    return false
  }


}
