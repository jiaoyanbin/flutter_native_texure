import 'package:flutter_module/imageload.dart';
import 'package:flutter_module/src/controller/base_controller.dart';

class ResImageViewController extends BaseController{

  ResImageViewController(String url) : super(url);

  @override
  Future<int> createNativeTextureId(String url, int oldTextureId, double width, double height, double aspectRatio, int scaleType) {
    return Imageload.createResImageTexture(url, oldTextureId,width, height, aspectRatio, scaleType);
  }
}
