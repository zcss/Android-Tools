# Android动画<第八篇>：转场动画与共享元素

当跳转Activity时，常常使用如下代码实现

```undefined
startActivity(intent);
```

这个代码是我们常见的了，但是跳转Activity还有另一个方法

```tsx
public void startActivity(Intent intent, @Nullable Bundle options)
```

这个带有`Bundle`参数的方法可以实现Activity的过度动画，第二个参数可以传递



```css
ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
```

但是，这个方法从Android5.0之后才推出，当我们做跳转动作时需要判断版本号，如下：



```kotlin
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            }else{
                startActivity(intent);
            }
```

仅仅使用`ActivityOptions`是看不到动画的，需要配合`Transition`才能看到动画效果。

Activity之间的切换（过渡），可以看成场景和场景之间的切换（过渡），在切换（过渡）过程中可以添加`Transition`实现变换操作。

从`makeSceneTransitionAnimation`字面上的意思可以看出：

`make`：制作
`Scene`：场景
`Transition`：过渡、切换、变换
`Animation`：动画

连起来的意思是：`制作场景过渡动画`，我把场景和场景之间的过渡动画称之为`转场动画`。

`makeSceneTransitionAnimation`的第二个参数是一个可变长度的数组，源码如下：



```java
@SafeVarargs
public static ActivityOptions makeSceneTransitionAnimation(Activity activity,
        Pair<View, String>... sharedElements) {
    ActivityOptions opts = new ActivityOptions();
    makeSceneTransitionAnimation(activity, activity.getWindow(), opts,
            activity.mExitTransitionListener, sharedElements);
    return opts;
}
```

`sharedElements`参数称之为`共享元素`。

`转场动画`和`共享元素`是本章的重点。

#### （1）转场动画

首先，设置下主题：



```xml
<style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
    <!-- Customize your theme here. -->
    <item name="colorPrimary">@color/colorPrimary</item>
    <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
    <item name="colorAccent">@color/colorAccent</item>

    <!--允许使用transitions -->
    <item name="android:windowContentTransitions">true</item>
    
    <!--true:同时执行，false：顺序执行-->
    <item name="android:windowAllowEnterTransitionOverlap">false</item>
    <item name="android:windowAllowReturnTransitionOverlap">false</item>
</style>
```

</resources>

`windowContentTransitions`默认为true，意思是：是否允许使用transitions 。（不过这个属性应该没有作用了，我设置成false，transitions 依然可以使用）
`windowAllowEnterTransitionOverlap`默认为true，true为创建场景时，前后两个场景动画同时执行。false为创建场景时，前后两个场景动画顺序执行。
`windowAllowReturnTransitionOverlap`默认为true，true为销毁场景时，前后两个场景动画同时执行。false为销毁场景时，前后两个场景动画顺序执行。

想要实现动画，还需要用到`Transition`对象，`Transition`的完整包名是`android.transition.Transition`，是Android SDK自带的接口。

`Transition`有三个间接子类，分别是：`Explode动画`、`Fade动画`、`Slide动画`。

根据源码注释，Explode和Slide都是移入移出的意思，Fade是淡入淡出的意思。

那么，这三种动画该怎么去实现呢？需要结合`ActivityOptions`的`makeSceneTransitionAnimation`方法。

|    N     |                          Slide动画                           |                         Explode动画                          |                           Fade动画                           |
| :------: | :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: |
| 效果展示 | ![img](https://upload-images.jianshu.io/upload_images/12648131-7dd08c1121e4a29d.gif?imageMogr2/auto-orient/strip\|imageView2/2/w/306/format/webp)167.gif | ![img](https://upload-images.jianshu.io/upload_images/12648131-6b8c072a503c3995.gif?imageMogr2/auto-orient/strip\|imageView2/2/w/306/format/webp)168.gif | ![img](https://upload-images.jianshu.io/upload_images/12648131-edabdce115886ae3.gif?imageMogr2/auto-orient/strip\|imageView2/2/w/306/format/webp)169.gif |
| 详细说明 | `Slide动画`从边缘开始移入移出动画，默认从下方开始显示，可以用代码控制的方向有：左、上、右、下 |       `Explode动画`从中间开始移入移出动画，它没有方向        |         `Fade动画`官方注释给出的解释是淡入淡出的意思         |





```
[Slide代码]
```

跳转到SceneTransitionActivity界面：



```kotlin
            Intent intent = new Intent(MainActivity.this, SceneTransitionActivity.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
            }else{
                startActivity(intent);
            }
```

SceneTransitionActivity中需要设置：



```cpp
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        Slide slide = new Slide();
        slide.setDuration(1000);
        slide.setSlideEdge(Gravity.LEFT);
        //创建Activity时的动画
        getWindow().setEnterTransition(slide);
        //销毁Activity时的过渡动画
        getWindow().setReturnTransition(slide);
        //从前台切换到后台时的动画
        getWindow().setExitTransition(slide);
        //Activity从后台切换到前台的动画
        getWindow().setReenterTransition(slide);
    }
[Explode代码]
```

跳转到SceneTransitionActivity界面：



```kotlin
            Intent intent = new Intent(MainActivity.this, SceneTransitionActivity.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
            }else{
                startActivity(intent);
            }
```

SceneTransitionActivity中需要设置：



```cpp
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        Explode explode = new Explode();
        explode.setDuration(1000);
        //创建Activity时的动画
        getWindow().setEnterTransition(explode);
        //销毁Activity时的过渡动画
        getWindow().setReturnTransition(explode);
        //从前台切换到后台时的动画
        getWindow().setExitTransition(explode);
        //Activity从后台切换到前台的动画
        getWindow().setReenterTransition(explode);
    }
[Fade代码]
```

跳转到SceneTransitionActivity界面：



```kotlin
            Intent intent = new Intent(MainActivity.this, SceneTransitionActivity.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
            }else{
                startActivity(intent);
            }
```

SceneTransitionActivity中需要设置：



```cpp
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        Fade fade = new Fade();
        fade.setDuration(3000);
        //创建Activity时的动画
        getWindow().setEnterTransition(fade);
        //销毁Activity时的过渡动画
        getWindow().setReturnTransition(fade);
        //从前台切换到后台时的动画
        getWindow().setExitTransition(fade);
        //Activity从后台切换到前台的动画
        getWindow().setReenterTransition(fade);
    }
```

以上三种动画用Java代码炫酷的实现了Activity的转场动画，那么除了使用Java代码实现之外还可以使用style方式实现：

样式：



```xml
<style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">

    <item name="android:windowEnterTransition">@transition/transition_explode</item>
    <item name="android:windowReturnTransition">@transition/transition_fade</item>
    <item name="android:windowExitTransition">@transition/transition_fade</item>
    <item name="android:windowReenterTransition">@transition/transition_explode</item>
</style>
```

布局文件：

![img](https://upload-images.jianshu.io/upload_images/12648131-55d36ab4f830c19f.png?imageMogr2/auto-orient/strip|imageView2/2/w/315/format/webp)

图片.png

transition_explode.xml



```xml
<?xml version="1.0" encoding="utf-8"?>
<explode xmlns:android="http://schemas.android.com/apk/res/android"
    android:duration="1000" />
```

transition_fade.xml



```xml
<?xml version="1.0" encoding="utf-8"?>
<fade
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:duration="3000"
/>
```

transition_slide.xml



```xml
<?xml version="1.0" encoding="utf-8"?>
<slide xmlns:android="http://schemas.android.com/apk/res/android"
    android:duration="1000" />
```

#### （2）共享元素

`共享元素`和`转场动画`是两个不同的概念，`转场动画`是使用Transition对象产生的场景开始到结束的过渡动画，而`共享元素`是场景开始（场景A）的View和场景结束（场景B）的View相互绑定。

那么该如何绑定两个场景的View呢？

为了防止`Transition`混淆演示图效果，我暂时将`Transition`去掉，仅仅使用`共享元素`完成动画。

从上面的讲解，我们知道了Activity之间的跳转其实就是场景的切换，场景的切换动画是由`Transition`来实现的，那么`共享元素`也能实现特殊的动画。

startActivity的第二个参数的ActivityOptions.makeSceneTransitionAnimation方法可以传递共享元素，代码如下：

场景A跳转代码，将场景A的一个`view`和一个`transitionName`传递到场景二



```cpp
View transition_view = findViewById(R.id.image_a);
String transition_name = getString(R.string.transition_name_image);
startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, transition_view, transition_name).toBundle());
```

当然，如果有多个共享元素，可以使用Pair对象



```css
ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, Pair.create(transition_view, transition_name),  Pair.create(transition_view, transition_name)).toBundle()
```



当切换到场景二时，其中的某View也必须标注`transitionName`



```objectivec
<ImageView
    android:id="@+id/image_b"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:src="@mipmap/ani_test"
    android:transitionName="@string/transition_name_image"
    android:layout_centerInParent="true"/>
```



两者相互绑定的View必须具有相同的`transitionName`（名字随便命名）



```cpp
<string name="transition_name_image">image</string>
```

这样，场景A和场景B之间的元素形成了共享，这就是`共享元素`。

效果如下：

![img](https://upload-images.jianshu.io/upload_images/12648131-9fd369ce7978bb71.gif?imageMogr2/auto-orient/strip|imageView2/2/w/306/format/webp)

171.gif

从效果图中，可以明显的看出，图形1和图形2之间的动画效果。

`转场动画`和`共享元素`结合使用效果如下：

![img](https://upload-images.jianshu.io/upload_images/12648131-d72aa11222396a52.gif?imageMogr2/auto-orient/strip|imageView2/2/w/306/format/webp)

172.gif

我想大家都能看出，`转场动画`的时间和`共享元素过渡动画`的时间不一样，那是因为我将`转场动画`的时间设置为了1秒，而`共享元素过渡动画`的时间是系统默认。

那么，怎么调整`共享元素过渡动画`的时间呢？

我们可以设置style来调整共享元素的时间，代码如下：



```xml
<style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
    <!-- Customize your theme here. -->
    <item name="colorPrimary">@color/colorPrimary</item>
    <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
    <item name="colorAccent">@color/colorAccent</item>

    <!--允许使用transitions -->
    <item name="android:windowContentTransitions">true</item>

    <!--true:同时执行，false：顺序执行-->
    <item name="android:windowAllowEnterTransitionOverlap">false</item>
    <item name="android:windowAllowReturnTransitionOverlap">false</item>

    <!--转场动画设置-->
    <item name="android:windowEnterTransition">@transition/transition_explode</item>
    <item name="android:windowReturnTransition">@transition/transition_explode</item>
    <item name="android:windowExitTransition">@transition/transition_explode</item>
    <item name="android:windowReenterTransition">@transition/transition_explode</item>


    <!--共享元素动画设置-->
    <item name="android:windowSharedElementExitTransition">@transition/transition_set</item>
    <item name="android:windowSharedElementReenterTransition">@transition/transition_set</item>
    <item name="android:windowSharedElementEnterTransition">@transition/transition_set</item>
    <item name="android:windowSharedElementReturnTransition">@transition/transition_set</item>

</style>
```

其中，主要看最下面四个，`共享元素动画设置`。

transition_set.xml



```xml
<?xml version="1.0" encoding="utf-8"?>
<transitionSet
    xmlns:android="http://schemas.android.com/apk/res/android">
    <!--为目标视图的布局边界的变化添加动画-->
    <changeBounds android:duration="2000"/>
    <!--为目标视图的裁剪边界的变化添加动画-->
    <changeTransform android:duration="2000"/>
    <!--为目标视图的缩放与旋转变化添加动画-->
    <changeClipBounds android:duration="2000"/>
    <!--为目标图像的大小与缩放变化添加动画-->
    <changeImageTransform android:duration="2000"/>
</transitionSet>
```

至于四个共享元素动画的配置是什么意思，看完文章上面部分，我想都能猜到。transitionSet属性下的`changeBounds`、`changeTransform`、`changeClipBounds`、`changeImageTransform`都有注释，这里不详细描述了。

最终效果如下：

![img](https://upload-images.jianshu.io/upload_images/12648131-0a6c9eecd447cedd.gif?imageMogr2/auto-orient/strip|imageView2/2/w/306/format/webp)

173.gif

#### （3）Fragment之间实现过渡动画

上面介绍了Activity之间的过渡动画，Fragment之间的过渡动画其实也差不多。

当Fragment切换时，的`Transition`动画



```cpp
        Slide slideTransition = new Slide(Gravity.LEFT);
        slideTransition.setDuration(1000);
        demo2Fragment.setEnterTransition(slideTransition);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, demo2Fragment)
                .commit();
```

效果如下：

![img](https://upload-images.jianshu.io/upload_images/12648131-6526b69815572a2e.gif?imageMogr2/auto-orient/strip|imageView2/2/w/306/format/webp)

174.gif

它的效果和Activity切换效果看起来差不多。那么`共享元素` 动画该怎么去实现呢？

代码如下：

从Fragment1切换到Fragment2



```csharp
public void jumpToFragment2(View view){

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

        Slide slideTransition = new Slide(Gravity.LEFT);
        slideTransition.setDuration(1000);
        demo2Fragment.setEnterTransition(slideTransition);

        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.transition_set);
        demo2Fragment.setSharedElementEnterTransition(transition);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, demo2Fragment)
                .addSharedElement(view, getString(R.string.transition_name_image))
                .commit();
    }else{
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, demo2Fragment)
                .commit();
    }
}
```

从Fragment2切换到Fragment1



```csharp
public void jumpToFragment1(View view){

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

        Slide slideTransition = new Slide(Gravity.RIGHT);
        slideTransition.setDuration(1000);
        demo1Fragment.setEnterTransition(slideTransition);

        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.transition_set);
        demo1Fragment.setSharedElementEnterTransition(transition);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, demo1Fragment)
                .addSharedElement(view, getString(R.string.transition_name_image))
                .commit();
    }else{
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, demo1Fragment)
                .commit();
    }
}
```

在fragment1中的某view中绑定共享元素名称：transitionName



```objectivec
<ImageView
    android:id="@+id/image_a"
    android:layout_width="50dp"
    android:layout_height="50dp"
    android:src="@mipmap/ani_test"
    android:transitionName="@string/transition_name_image"
    android:layout_centerVertical="true"/>
```

在fragment2中的某view中绑定共享元素名称：transitionName



```objectivec
<ImageView
    android:id="@+id/image_b"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:src="@mipmap/ani_test"
    android:transitionName="@string/transition_name_image"
    android:layout_centerInParent="true"/>
```

我们先删除如下代码：



```cpp
    Slide slideTransition = new Slide(Gravity.LEFT);
    slideTransition.setDuration(1000);
    demo2Fragment.setEnterTransition(slideTransition);
```

也就是删除`Transition动画`相关代码，效果如下：

![img](https://upload-images.jianshu.io/upload_images/12648131-1bc365ee8da0bbd8.gif?imageMogr2/auto-orient/strip|imageView2/2/w/306/format/webp)

175.gif

加上`Transition动画`之后的效果如下：

![img](https://upload-images.jianshu.io/upload_images/12648131-565f285184ff6b01.gif?imageMogr2/auto-orient/strip|imageView2/2/w/306/format/webp)

176.gif

最后，别忘了以下这两个方法（上面有介绍）



```bash
        demo1Fragment.setAllowEnterTransitionOverlap(false);
        demo1Fragment.setAllowReturnTransitionOverlap(false);
```

#### （4）布局间的场景动画

`[第一步]` 创建场景



```undefined
Scene scene1 = Scene.getSceneForLayout(ll_yuan, R.layout.scene1, getActivity());
```

`[第二步]` 创建Transition



```cpp
                Fade fade = new Fade(Fade.OUT);
                fade.setDuration(3000);
```

`[第三步]` 执行切换场景动画（三种方式）



```go
        TransitionManager.go(scene1, fade);
        //TransitionManager.go(scene1, TransitionInflater.from(getActivity()).inflateTransition(R.transition.transition_set));
        //TransitionManager.go(scene1);
```

效果如下：

![img](https://upload-images.jianshu.io/upload_images/12648131-bc8ddb83b3c69fc3.gif?imageMogr2/auto-orient/strip|imageView2/2/w/270/format/webp)

177.gif

除了多个场景之间的过渡动画之外，还可以使用更改某一个场景的属性触发属性变化的动画。

```
[第一步]
```



```css
TransitionManager.beginDelayedTransition
```

`[第二步]` 修改view属性

代码如下：

按钮一：



```csharp
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                ChangeBounds changeBounds = new ChangeBounds();
                changeBounds.setDuration(3000);
                TransitionManager.beginDelayedTransition(ll_yuan, changeBounds);
                ViewGroup.LayoutParams params = image_yuan.getLayoutParams();
                params.width = 600;
                image_yuan.setLayoutParams(params);
            }
```

按钮二：



```csharp
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                ChangeBounds changeBounds = new ChangeBounds();
                changeBounds.setDuration(3000);
                TransitionManager.beginDelayedTransition(ll_yuan, changeBounds);
                ViewGroup.LayoutParams params = image_yuan.getLayoutParams();
                params.width = 100;
                image_yuan.setLayoutParams(params);
            }
```

效果如下：

![img](https://upload-images.jianshu.io/upload_images/12648131-ce813cc83e602732.gif?imageMogr2/auto-orient/strip|imageView2/2/w/270/format/webp)

178.gif