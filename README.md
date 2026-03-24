## 使用方法
### 复制aar到libs
### Application的OnCreate()添加以下代码
```
    DebugDB.initialize(this,null);
    Log.e("-----------------", "地址 "+DebugDB.getAddressLog());
    Pandora.get().open();
    DoraemonKit.install(this);
```
