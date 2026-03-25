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

[DoKit](https://github.com/didi/DoKit):
![Dokit](https://camo.githubusercontent.com/241f8d6b321eac44db2f771da50f1a5bc33185daa3f9580271fab0c90711611b/68747470733a2f2f70742d73746172696d672e646964697374617469632e636f6d2f7374617469632f73746172696d672f696d672f57525564695744737737313632363639363334343638302e6a7067)

[DebugDB](https://github.com/zcss/Android-Debug-Database)
![DebugDB](https://raw.githubusercontent.com/amitshekhariitbhu/Android-Debug-Database/master/assets/debugdb.png)

![DebugDB](https://raw.githubusercontent.com/amitshekhariitbhu/Android-Debug-Database/master/assets/debugdb_edit.png)
