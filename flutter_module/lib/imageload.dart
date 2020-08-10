import 'dart:async';
export 'package:flutter_module/src/controller/base_controller.dart';
export 'package:flutter_module/src/controller/network_controller.dart';
export 'package:flutter_module/src/controller/resource_controller.dart';
export 'package:flutter_module/src/view/image_view.dart';
export 'package:flutter_module/src/view/scale_type.dart';

import 'package:flutter/services.dart';

class Imageload {
  static const MethodChannel _channel = const MethodChannel('imageload');

  static Future<int> createNetImageTexture(String url,int oldTextureId, double width, double height,
      double aspectRatio, int scaleType) async {
    final int textureId = await _channel.invokeMethod('createNetImageTexture', {
      'oldTextureId': oldTextureId,
      'width': width,
      'height': height,
      'url': url,
      'aspectRatio': aspectRatio,
      'scaleType': scaleType
    });
    return textureId;
  }

  static Future<int> createResImageTexture(String drawable, int oldTextureId,double width, double height,
      double aspectRatio, int scaleType) async {
    final int textureId = await _channel.invokeMethod('createResImageTexture', {
      'oldTextureId': oldTextureId,
      'width': width,
      'height': height,
      'url': drawable,
      'aspectRatio': aspectRatio,
      'scaleType': scaleType
    });
    return textureId;
  }

  static Future<int> destoryImageTexture(int textureId) async {
    _channel.invokeMethod('destoryImageTexture', {'textureId': textureId});
  }
}
