import 'package:flutter_module/imageload.dart';
import 'package:flutter_module/src/view/scale_type.dart';

typedef void OnImageCallbackListener(int textureId);

abstract class BaseController {
    int textureId;
    final String _url;
    bool disposed = false;
    OnImageCallbackListener _onImageCallbackListener;

    BaseController(this._url);


    set onImageCallbackListener(OnImageCallbackListener listener) {
      _onImageCallbackListener = listener;
    }

    Future loadImage({int oldTextureId, double width, double height, double aspectRatio, int scaleType = ScaleType.NORMAL}) async {
      textureId = await createNativeTextureId(_url, oldTextureId,width, height, aspectRatio, scaleType);
      if (disposed) {
        this.dispose();
      } else {
        if(_onImageCallbackListener!=null){
          _onImageCallbackListener(textureId);
        }
      }
    }

    void dispose() {
      Imageload.destoryImageTexture(textureId);
      disposed = true;
    }

    Future<int> createNativeTextureId(String url,int oldTextureId,double width, double height, double aspectRatio, int scaleType );
}
