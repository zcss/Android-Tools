## 使用方法
### 复制“打包好的”目录下的AAR到libs
### Application的OnCreate()添加以下代码
```
    //使用后app中的WebView能可上不了网
    DebugDB.initialize(this,null);//数据库工具
    Log.e("-----------------", "地址 "+DebugDB.getAddressLog());
    Pandora.get().open();//UI工具-
    DoraemonKit.install(this);//集合工具
```
## 源码地址：
多功能
[DoKit](https://github.com/didi/DoKit):
https://github.com/didi/DoKit

UI查看工具
[pandora](https://github.com/whataa/pandora):
https://github.com/whataa/pandora

数据库工具
[Database](https://github.com/zcss/Android-Debug-Database)
https://github.com/zcss/Android-Debug-Database
