# Android 打开外部高德、百度、腾讯地图进行导航

#### 前言

- 由于项目中有用到位置信息，最近闲来无事，决定加个导航功能（反正没有产品经理，随便折腾）；考虑到内部集成成本过高，于是决定调用外部地图。

- 至于地图，给出了高德地图、百度地图、腾讯地图供用户选择，算是目前主流的三个地图吧，相信大多数用户都会有其中的一两个吧。

  ![img](https:////upload-images.jianshu.io/upload_images/10346127-fb2768db329e099a.jpeg?imageMogr2/auto-orient/strip|imageView2/2/w/1080/format/webp)

  地图选择.jpeg

#### 说明

- 本文介绍的是公交出行的路线规划，如需选择其它出行方式，修改相关配置就好，文章中有给出，可也参考官方文档。
- 这里的起始位置，均使用的外部地图的当前位置，操作简单；也可以自行设置起始位置。

#### 高德地图

- [高德地图官方 api 戳这里  👈](https://link.jianshu.com?t=http%3A%2F%2Flbs.amap.com%2Fapi%2Famap-mobile%2Fguide%2Fandroid%2Froute)

- 效果如下：

  ![img](https:////upload-images.jianshu.io/upload_images/10346127-e162ca9c7e7b8eda.gif?imageMogr2/auto-orient/strip|imageView2/2/w/360/format/webp)

  高德地图.gif

- 想省事看这，拷走就好，重要参数有说明：



```dart
/**
 * 打开高德地图（公交出行，起点位置使用地图当前位置）
 * 
 * t = 0（驾车）= 1（公交）= 2（步行）= 3（骑行）= 4（火车）= 5（长途客车）
 *
 * @param dlat  终点纬度
 * @param dlon  终点经度
 * @param dname 终点名称
 */
private void openGaoDeMap(double dlat, double dlon, String dname) {
    if (checkMapAppsIsExist(this, "com.autonavi.minimap")) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setPackage("com.autonavi.minimap");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(Uri.parse("androidamap://route?sourceApplication=" + R.string.app_name
                + "&sname=我的位置&dlat=" + dlat
                + "&dlon=" + dlon
                + "&dname=" + dname
                + "&dev=0&m=0&t=1"));
        startActivity(intent);
    } else {
        ToastUtils.makeText(this, "高德地图未安装", false).show();
    }
}
```

#### 百度地图

- [百度地图官方 api 戳这里  👈](https://link.jianshu.com?t=http%3A%2F%2Flbsyun.baidu.com%2Findex.php%3Ftitle%3Duri%2Fapi%2Fandroid)

- 效果如下，其实类似：

  ![img](https:////upload-images.jianshu.io/upload_images/10346127-6fa76212fcdb286f.gif?imageMogr2/auto-orient/strip|imageView2/2/w/360/format/webp)

  百度地图.gif

  代码在这：



```dart
 * 打开百度地图（公交出行，起点位置使用地图当前位置）
 * 
 * mode = transit（公交）、driving（驾车）、walking（步行）和riding（骑行）. 默认:driving
 * 当 mode=transit 时 ： sy = 0：推荐路线 、 2：少换乘 、 3：少步行 、 4：不坐地铁 、 5：时间短 、 6：地铁优先
 *
 * @param dlat  终点纬度
 * @param dlon  终点经度
 * @param dname 终点名称
 */
private void openBaiduMap(double dlat, double dlon, String dname) {
    if (checkMapAppsIsExist(this, "com.baidu.BaiduMap")) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("baidumap://map/direction?origin=我的位置&destination=name:"
                + dname
                + "|latlng:" + dlat + "," + dlon
                + "&mode=transit&sy=3&index=0&target=1"));
        startActivity(intent);
    } else {
        ToastUtils.makeText(this, "百度地图未安装", false).show();
    }
}
```

#### 腾讯地图

- [腾讯地图官方 api 戳这里  👈](https://link.jianshu.com?t=http%3A%2F%2Flbs.qq.com%2Furi_v1%2Fguide-route.html)

- 官方暂时只支持打开web端，并不支持外部打开腾讯地图app（合作伙伴可以），但还是找到解决方法。

- 参考：[Android在本地应用唤起第三方地图](https://www.jianshu.com/p/ea092e243aaa)

- 效果如下，其实也类似：

  ![img](https:////upload-images.jianshu.io/upload_images/10346127-193d047ddbc343e0.gif?imageMogr2/auto-orient/strip|imageView2/2/w/360/format/webp)

  腾讯地图.gif

- 代码在这：



```dart
/**
 * 打开腾讯地图（公交出行，起点位置使用地图当前位置）
 * 
 * 公交：type=bus，policy有以下取值
 * 0：较快捷 、 1：少换乘 、 2：少步行 、 3：不坐地铁
 * 驾车：type=drive，policy有以下取值
 * 0：较快捷 、 1：无高速 、 2：距离短
 * policy的取值缺省为0
 *
 * @param dlat  终点纬度
 * @param dlon  终点经度
 * @param dname 终点名称
 */
private void openTencent(double dlat, double dlon, String dname) {
    if (checkMapAppsIsExist(this, "com.tencent.map")) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("qqmap://map/routeplan?type=bus&from=我的位置&fromcoord=0,0"
                + "&to=" + dname
                + "&tocoord=" + dlat + "," + dlon
                + "&policy=1&referer=myapp"));
        startActivity(intent);
    } else {
        ToastUtils.makeText(this, "腾讯地图未安装", false).show();
    }
}
```

#### 检测地图是否安装

- 当然这里还用到了包检测工具：



```java
/**
 * 检测地图应用是否安装
 *
 * @param context
 * @param packagename
 * @return
 */
public boolean checkMapAppsIsExist(Context context, String packagename) {
    PackageInfo packageInfo;
    try {
        packageInfo = context.getPackageManager().getPackageInfo(packagename, 0);
    } catch (Exception e) {
        packageInfo = null;
        e.printStackTrace();
    }
    return packageInfo != null;
}
```

作者：呱呱_
链接：https://www.jianshu.com/p/9bdb2d519309
来源：简书
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。