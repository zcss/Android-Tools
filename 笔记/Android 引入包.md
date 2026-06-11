Android 引入包

### Glide：

```
dependencies {
    implementation 'com.github.bumptech.glide:glide:4.12.0' // 确保使用最新版本
    annotationProcessor 'com.github.bumptech.glide:compiler:4.12.0' // 注解处理器，用于生成代码
}
```

### android.support.design.widget.TabLayout

```
dependencies {
    implementation 'com.android.support:design:28.0.0' // 确保版本号与你的SDK版本兼容
}
```

### V7

```
implementation 'com.android.support:recyclerview-v7:28.0.0'
implementation 'com.android.support:appcompat-v7:28.0.0'
```

### 引入AAR / JAR

```
implementation fileTree(dir: 'libs', include: ['*.jar', '*.aar'])
```

### support-annotations

```
implementation 'com.android.support:support-annotations:28.0.0'
```

### recyclerview-v7

```
implementation 'com.android.support:recyclerview-v7:28.0.0'
implementation 'com.android.support:appcompat-v7:28.0.0'
```

### Glide

```
implementation 'com.github.bumptech.glide:glide:4.12.0'
```

