package com.tools2;

import android.app.Application;
import android.util.Log;


import com.amitshekhar.DebugDB;
import com.didichuxing.doraemonkit.DoraemonKit;
import com.didichuxing.doraemonkit.kit.AbstractKit;

import java.util.ArrayList;

import tech.linjiang.pandora.Pandora;
public class AppMain extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Pandora.get().open();
//        DoraemonKit.install(this);
        DoraemonKit.install(this,"749a0600b5e48dd77cf8ee680be7b1b7");
        DebugDB.initialize(this,null);
        Log.e("----","DB： "+DebugDB.getAddressLog());
//        Pandora.init(this).enableShakeOpen(); // 摇动设备启动调试
//        DebugDb
//            DoraemonKit.install(this);
        ArrayList<AbstractKit> kits = new ArrayList();
//        kits.add(DemoKit());
//        kits.add(TestSimpleDokitFloatViewKit());
//        kits.add(TestSimpleDokitFragmentKit());

//        LinkedHashMap<String, List<AbstractKit>> mapKits = new LinkedHashMap<>();
//        mapKits["业务专区1"] = mutableListOf<AbstractKit>().apply {
//            add(DemoKit())
//            add(TestSimpleDokitFloatViewKit())
//            add(TestSimpleDokitFragmentKit())
//        }

//        mapKits["业务专区2"] = mutableListOf<AbstractKit>(DemoKit())



//        new DoKit.Builder(this)
//                .productId("749a0600b5e48dd77cf8ee680be7b1b7")
//                //测试环境pid
////            .productId("277016abcc33bff1e6a4f1afdf14b8e1")
//                .disableUpload()
//                .customKits(mapKits)
//                .fileManagerHttpPort(9001)
////                .databasePass(mapOf("Person.db" to "a_password"))
//                .mcWSPort(5555)
//                .alwaysShowMainIcon(true)
//                .callBack(new DoKitCallBack() {
//                    @Override
//                    public void onCpuCallBack(float v, @NonNull String s) {
//
//                    }
//
//                    @Override
//                    public void onFpsCallBack(float v, @NonNull String s) {
//
//                    }
//
//                    @Override
//                    public void onMemoryCallBack(float v, @NonNull String s) {
//
//                    }
//
//                    @Override
//                    public void onNetworkCallBack(@NonNull NetworkRecord networkRecord) {
//
//                    }
//                })
//            .build();
    }
}
