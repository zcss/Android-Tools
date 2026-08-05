## 一，Lifecycle 协成

### 1. MainScope

   ```
   val scope = MainScope();
   scope.launch(Dispatchers.IO) {
       delay(200) //模拟长时间执行任务
       withContext(Dispatchers.Main) {
           // 更新UI
       }
   }
   ```

   

### 2 GlobalScope

   ```
   GlobalScope.launch(Dispatchers.IO) {
       delay(200) //模拟长时间执行任务
       withContext(Dispatchers.Main) {
           // 更新UI
       }
   }
   ```

   

### 3 lifecycleScope

  ```
   lifecycleScope.launch (Dispatchers.IO) {
       delay(200) //模拟长时间执行任务
       withContext(Dispatchers.Main) {
           // 更新UI
       }
   }
  ```

### 4 ViewModel 专属
```
class MyViewModel : ViewModel() {
    fun loadData() {
        viewModelScope.launch {
            // 自动随 ViewModel 销毁而取消
        }
    }
}

```

### 总结

| 特性            | lifecycleScope           | MainScope                  | GlobalScope                       |
| --------------- | ------------------------ | -------------------------- | --------------------------------- |
| ‌生命周期绑定‌    | ‌绑定 Activity/Fragment‌   | ‌手动控制‌ (需手动取消)      | ‌无绑定‌ (应用进程级别)             |
| ‌自动取消时机‌    | Activity/Fragment 销毁时 | 调用 cancel() 时           | 应用进程结束时                    |
| ‌默认 Dispatcher‌ | Dispatchers.Main         | Dispatchers.Main           | Dispatchers.Default               |
| ‌内存泄漏风险‌    | ‌低‌ (自动清理)            | ‌中‌ (忘记取消则泄漏)        | ‌高‌ (极易导致泄漏)                 |
| ‌推荐场景‌        | UI 相关操作、网络请求    | 自定义组件、非 UI 长期任务 | ‌几乎不推荐‌ (仅用于全局单例或测试) |

### CoroutineScope

| 具体 Scope     | 本质                    | 特点                                             |
| -------------- | ----------------------- | ------------------------------------------------ |
| lifecycleScope | LifecycleCoroutineScope | 内部持有 Job，监听 Lifecycle 状态自动 cancel     |
| viewModelScope | CoroutineScope          | 内部持有 Job，监听 ViewModel 清除事件自动 cancel |
| MainScope()    | CoroutineScope          | 简单工厂方法，返回 Dispatchers.Main + Job()      |
| GlobalScope    | CoroutineScope          | 单例，Job 为空，不绑定任何生命周期               |

## 二，ViewModel

```kotlin
class MyViewModel: ViewModel() {
    private val  data = MutableLiveData<String>()
    public fun getData(): MutableLiveData<String> {
        return data
    }
    public fun setData(st: String) {
        data.value = st
    }
}

Activity:
val vm: MyViewModel = ViewModelProvider(this).get(MyViewModel::class.java)
vm.getData().observe(this,{
    // 更新UI
})

//修改ViewModel
vm.setData("触发更新") //即使屏幕旋转，也是可以从ViewModel取得上次设置的值

```

## 三，LiveData反向绑定

- 在layout.xml中添加layout

```xml
// 外层设置layout标签
<layout>
	<data>
    	<variable name = "data" type = "com.entity.MyViewModel" />
    </data>
	// 原有布局代码
    ……
    <TextView
          ……
          android:text = "@{data.getData()}"
          ></TextView>
    <Button
          ……
          android:onClick = "@{data.update}"
          ></Button>
</layout>
```

- 在Activity中设置ViewModel

 ```kotlin
 fun onCreate(...) {
     val vm: MyViewModel = ViewModelProvider(this).get(MyViewModel::class.java)
     activityMainBinding.setData(vm)
     activityMainBinding.setLifecycleOwner(this)//绑定生命周期
 }
 
 ```

  ## 四，ROOM

* 引入

    ```groovy
    def room_version = "2.6.1" // 请使用最新版本
    
    implementation "androidx.room:room-runtime:$room_version"
    implementation "androidx.room:room-ktx:$room_version"
    ```

* 创建Entity（需要添加get/set，如果不添加，会编译报错）

  ```java
  
  @Entity
  public class Word {
      @PrimaryKey(autoGenerate = true)
      private int id; // 创建主键
  
      @ColumnInfo(name = "word") // 列名，如果不会以变量名为列名
      private String word;
      
      @ColumnInfo(name = "name")
      private String name;
  
      public Word(String word, String name) {
          this.word = word;
          this.name = name;
      }
      public String getWord() {
          return word;
      }
  
      public void setWord(String word) {
          this.word = word;
      }
  
      public String getName() {
          return name;
      }
  
      public void setName(String name) {
          this.name = name;
      }
  }
  ```

* 创建Dao

  ```java
  public interface WordDao {
      @Insert
      void insertWords(List<Word> words); // 插入多个
  
      @Update
      int updateWords(Word... words); // 更新多个
  
      @Delete
      int deleteWords(Word... words); //删除多个
  
      @Query("DELETE FROM WORD")
      int deleteAll(); // 全部删除
  
      @Query("SELECT * FROM WORD ORDER BY ID DESC")
      List<Word> getAllWords();
  }
  ```

* 创建RoomDatabase

  ```java
  @Database(entities = {Word.class}, version = 1, exportSchema = false)
  public abstract class WordDataBase extends RoomDatabase {
      public abstract WordDao getWordDao();
  }
  ```

* 在Activity中使用

    ```java
    wordDataBase = Room.databaseBuilder(this, WordDataBase.class,"word_database")
                    .allowMainThreadQueries() // 强制在主线程远行
                    .build();
    wordDao = wordDataBase.getWordDao();
    
    // 调用 插入
    List<Word> list = new ArrayList<>();
    for (int f = 0; f < 10; f++) {
        list.add(new Word("w_" + f,"name_" + f));
    }
    wordDao.insertWords(list);
    ```

    

####  问题总结：

app/build.gradle

```groovy
plugins {
    alias(libs.plugins.android.application)
}
```

改为：

   ```groovy
    plugins {
        id 'com.android.application'
        id 'org.jetbrains.kotlin.android' version '1.9.20'
        id 'com.google.devtools.ksp' version '1.9.20-1.0.13'
    }
   ```

如果还有问题添加：

~~~groovy
```groovy
// Java 编译的目标版本（原配置保持 JDK 11）
compileOptions {
    sourceCompatibility JavaVersion.VERSION_11
    targetCompatibility JavaVersion.VERSION_11
}

// 新增 Kotlin 编译的 JVM 目标配置，和上面 Java 版本完全对齐
kotlinOptions {
    jvmTarget = '11'
}
```
~~~



## 五，Retrofit

* app/build.gradle

  ```groovy
  def retrofit_version = "2.9.0"
  implementation "com.squareup.retrofit2:retrofit:$retrofit_version"
  implementation "com.squareup.retrofit2:converter-gson:$retrofit_version" // JSON 解析
  implementation "com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2" // 协程支持
  ```

* API

  ```java
  public interface ApiService {
      @GET(参数)
      String getHttp(@Path("id") int userId);
  
      @GET("s")
      Call<String> getTest();
  }
  ```

* 调用

  ``` java
  public static ApiService getApiService(){
      Gson gson = new GsonBuilder()
              .setLenient() // 自动调用JsonReader.setLenient(true)
              .create();
      Retrofit retrofit = new Retrofit.Builder()
              .baseUrl("https://www.baidu.com/")
  //                .addConverterFactory(GsonConverterFactory.create(gson))
              .build();
  
      return retrofit.create(ApiService.class);
  }
  
  Call<String> call = getApiService().getTest();
      call.enqueue(new Callback<String>() {
          @Override
          public void onResponse(Call<String> call, Response<String> response) {
              String body = response.body();
              binding.tvMsg.setText(body);
          }
  
          @Override
          public void onFailure(Call<String> call, Throwable t) {
              Log.e("onFailure", t.toString(),t);
              t.printStackTrace();
              binding.tvMsg.setText(t.toString());
             }
      });
  
  
  ```

## 六，Glide

* app/build.gradle

  ```groovy
  implementation 'com.github.bumptech.glide:glide:4.16.0'
  annotationProcessor 'com.github.bumptech.glide:compiler:4.16.0'
  ```

* 使用

  ```java
              // 加载网络图片
  //            Glide.with(MainActivity.this)
  //                    .load("https://gips3.baidu.com/it/u=3886271102,3123389489&fm=3028&app=3028&f=JPEG&fmt=auto?w=1280&h=960")
  //                    .into(binding.ivTest);
  
              // 加载本地资源图片
  //            Glide.with(MainActivity.this)
  //                    .load(R.mipmap.ic_launcher)
  //                    .into(binding.ivTest);
  
              // 加载本地文件图片
  //            File imageFile = new File(getExternalFilesDir(null), "demo.jpg");
  //            Glide.with(MainActivity.this)
  //                    .load(imageFile)
  //                    .into(binding.ivTest);
  
              Glide.with(this)
                      .load("https://gips3.baidu.com/it/u=3886271102,3123389489&fm=3028&app=3028&f=JPEG&fmt=auto?w=1280&h=960")
                      .placeholder(R.mipmap.ic_launcher) // 加载过程中显示的占位图
  //                    .error(R.drawable.load_fail_bg) // 加载失败时显示的错误图
                      .override(300, 300) // 指定图片的目标宽高，避免加载过大的原图占用内存
                      .centerCrop() // 裁剪策略：居中填充，超出部分裁切
                  	//.fitCenter() //全显示，等比例缩放
                      .transition(DrawableTransitionOptions.withCrossFade(1500)) // 300ms淡入过渡动画
                      .into(binding.ivTest);
  ```

  ### 修改Glide配置

  * 1.创建类,`@GlideModule` 注解

    ```java
    import com.bumptech.glide.annotation.GlideModule;
    import com.bumptech.glide.module.AppGlideModule;
    import com.bumptech.glide.GlideBuilder;
    import com.bumptech.glide.load.DecodeFormat;
    import com.bumptech.glide.request.RequestOptions;
    import android.content.Context;
    import androidx.annotation.NonNull;
    
    @GlideModule
    public class MyAppGlideModule extends AppGlideModule {
        @Override
        public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
            // 1. 设置磁盘缓存大小为 250MB
            int diskCacheSize = 250 * 1024 * 1024;
            builder.setDiskCache(new InternalDiskCacheFactory(context, diskCacheSize));
    
            // 2. 设置内存缓存大小（默认通常足够，可根据需求调整）
            // builder.setMemoryCache(new LruResourceCache(memorySizeInBytes));
    
            // 3. 设置默认图片解码格式为 RGB_565（节省约50%内存，但无透明度支持）
            // 若需显示透明 PNG/GIF，请改为 PREFER_ARGB_8888
            RequestOptions defaultOptions = new RequestOptions()
                    .format(DecodeFormat.PREFER_RGB_565)
                    .disallowHardwareConfig(); // 禁用硬件位图以避免部分机型兼容问题
            
            builder.setDefaultRequestOptions(defaultOptions);
        }
    
        // 禁止解析 Manifest 中的其他 GlideModule，提升启动速度
        @Override
        public boolean isManifestParsingEnabled() {
            return false;
        }
    }
    
    ```

  * 重新构建项目

    修改后必须执行 ‌**Build -> Rebuild Project**‌，Glide 会生成 `GlideApp` 类，后续使用 `GlideApp.with()` 即可应用全局配置。







































