import 'dart:math';

import 'package:flutter/material.dart';
import 'package:flutter_module/imageload.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  static List urls = [
    'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1591941448546&di=9d25fb16bc5daf401f7c62c45ba3f77d&imgtype=0&src=http%3A%2F%2Fimage3.cnpp.cn%2Fupload%2Fimages%2F20190309%2F13342762753_1200x675.jpg',
    'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1591941448546&di=4704c4918dd68a20b052ffbfbbb6099b&imgtype=0&src=http%3A%2F%2Fimage3.cnpp.cn%2Fupload%2Fimages%2F20190309%2F13343395652_1200x500.jpg',
    'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1591941539232&di=1d6e8674543b0d794c3b2536b08410d7&imgtype=0&src=http%3A%2F%2Fimg3.imgtn.bdimg.com%2Fit%2Fu%3D1910418823%2C2989671173%26fm%3D214%26gp%3D0.jpg',
    'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1591941448545&di=a91ab973fa1ebbf5fe8a8f81bd619a19&imgtype=0&src=http%3A%2F%2Farticle.fd.zol-img.com.cn%2Ft_s640x2000%2Fg5%2FM00%2F0E%2F00%2FChMkJ1fE3yeISvtMAAB_7xuTDREAAU1OwPuOGYAAIAH313.jpg',
    'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1591941448545&di=d19de6e4c9c35c51c77cf6cb59a0a5bf&imgtype=0&src=http%3A%2F%2Fimage2.cnpp.cn%2Fupload%2Fimages%2F20190309%2F13343098132_1200x675.jpg',
    'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1592284441846&di=a5f8bb528ae9adda052fc73f44140725&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fforum%2Fw%3D580%2Fsign%3D78f73c101cd8bc3ec60806c2b28aa6c8%2F31f25982b2b7d0a218828db7ceef76094a369a93.jpg',
    'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1592284441845&di=6078a5aca1d8c490e05e41b460c47f7b&imgtype=0&src=http%3A%2F%2Fp1.gexing.com%2FG1%2FM00%2F71%2F17%2FrBACFFIKSgzjLEvhAAXSAEZWi5s156.jpg',
    'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1592284441839&di=fd3a6ad33852343aad3b3d63877eecda&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201503%2F03%2F20150303220413_xQmUB.jpeg',
  ];

  String imageUrl = urls[0];

  @override
  void initState() {
    super.initState();
  }

  @override
  void dispose() {
    // TODO: implement dispose
    super.dispose();
    print('--------main dispose------');
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              MaterialButton(
                color: Colors.blue,
                textColor: Colors.white,
                child: new Text('加载图片'),
                onPressed: () {
                  setState(() {
                    imageUrl = urls[Random().nextInt(urls.length - 1)];
                    print(imageUrl);
                  });
                },
              ),
              Container(
                width: 100,
                height: 100,
                child: ImageView(
                  NetImageViewController(imageUrl),
                  width: 100,
                  height: 100,
                ),
              ),
//
              Container(
                width: 100,
                height: 100,
                margin: EdgeInsets.only(top: 10),
                child: ImageView(
                  ResImageViewController('meng_meng'),
                  width: 100,
                  height: 100,
                ),
              ),
              Container(
                width: 100,
                height: 100,
                margin: EdgeInsets.only(top: 10),
                child: ImageView(
                  NetImageViewController(imageUrl),
                  width: 100,
                  height: 100,
                ),
              ),
              Container(
                width: 100,
                height: 100,
                margin: EdgeInsets.only(top: 10),
                child: ImageView(
                  ResImageViewController('userpic_man_default'),
                  width: 100,
                  height: 100,
                ),
              ),
              Container(
                width: 100,
                height: 100,
                margin: EdgeInsets.only(top: 10),
                child: ImageView(
                  ResImageViewController('meng_meng'),
                  width: 100,
                  height: 100,
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
