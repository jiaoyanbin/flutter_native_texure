import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import 'package:flutter_module/src/controller/base_controller.dart';
import 'package:flutter_module/src/view/scale_type.dart';

class ImageView extends StatefulWidget {
  final double width;
  final double height;
  final double aspectRatio;
  final int scaleType; //图片加载类型
  final BaseController controller;
  const ImageView(
      this.controller,
      {Key key,
      this.width,
      this.height,
      this.aspectRatio = 0,
      this.scaleType = ScaleType.NORMAL})
      : super(key: key);

  @override
  State<StatefulWidget> createState() {
    return _ImageViewState(controller);
  }
}

class _ImageViewState extends State<ImageView> {
  BaseController _controller;
  int _textureId = -1;

  _ImageViewState(controller){
    this._controller=controller;
    if(this._controller!=null){
      _controller.onImageCallbackListener= onImageCallbackListener;
    }
  }

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      width: widget.width,
      height: widget.height,
      child: Texture(
        textureId: _textureId,
      ),
    );
  }


  @override
  void didUpdateWidget(ImageView oldWidget) {
    super.didUpdateWidget(oldWidget);
  }

  void onImageCallbackListener(int textureId){
    setState(() {
      _textureId = textureId;
    });
  }

  @override
  void initState() {
    super.initState();
    _controller.loadImage(
        oldTextureId: _textureId,
        width: widget.width,
        height: widget.height,
        aspectRatio: widget.aspectRatio,
        scaleType: widget.scaleType);
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }
}
