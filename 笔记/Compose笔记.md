## 事例

### 显示文字

```kotlin
package com.example.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.compose.ui.theme.ComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge() // 沉浸式标题
        setContent {
            PreviewMessageCard("zs")
        }
    }


    //    @Preview
    @Composable
    fun  PreviewMessageCard(name: String) {
        Text(text = "Hello $name")
    }

	// 显示预览
    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        ComposeTheme {
            PreviewMessageCard("Android")
        }
    }
}
```

### 垂直布局Column

```kotlin
@Composable
fun  MessageCard(msg: Message) {
    Column() {
        Text(text = "Hello ${msg.author}")
        Text(text = "Hello ${msg.body}")
    }

}
```

### 水平布局

```kotlin
@Composable
fun  MessageCard(msg: Message) {
    Row {
        Image(
            painter = painterResource(id = R.mipmap.ic_launcher),
            contentDescription = null
        )
        Column() {
            Text(text = "Hello ${msg.author}")
            Text(text = "Hello ${msg.body}")
        }
    }
}
```

### 控件的属性修改 Modifier

``` kotlin
Row(modifier = Modifier.padding(10.dp)) {
    Image(
        painter = painterResource(id = R.mipmap.ic_launcher),
        contentDescription = null,
        modifier = Modifier.size(80.dp)
            // 圆角大小为20dp
            .clip(RoundedCornerShape(80.dp))
//                    .clip(CircleShape)
    )
    Column() {
        Text(text = "Hello ${msg.author}")
        //添加一个间距
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Hello ${msg.body}")
    }
}
}
```

| 形状代码                 | 效果     | 适用场景             |
| ------------------------ | -------- | -------------------- |
| CircleShape              | 正圆     | 头像、圆形图标       |
| RoundedCornerShape(8.dp) | 8dp 圆角 | 卡片、按钮、常规图片 |
| RoundedCornerShape(50)   | 50% 圆角 | 胶囊形、椭圆         |
| CutCornerShape(8.dp)     | 切角     | 特殊设计风格         |

### Material Design 颜色、排版、形状

``` kotlin
// 在Theme.kt中修改DarkColorScheme和LightColorScheme
// 添加属性background = Color.Gray 

Row(modifier = Modifier.padding(10.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Image(
            painter = painterResource(id = R.mipmap.ic_launcher),
            contentDescription = null,
            modifier = Modifier.size(80.dp)
                .clip(CircleShape)
        )
        Column() {
            Text(text = "Hello ${msg.author}")
            //添加一个间距
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Hello ${msg.body}")
        }
    }
```

### 带缓冲的列表

``` kotlin

// 组装数据
val list = mutableListOf<Message>()
for (i in 1..50) {
    list.add(Message("android: $i","packjetpackjetpackjetpack"))
}
Conversation(list)

@Composable
fun Conversation(msgs: List<Message>) {
    LazyColumn() {
        items(msgs) { message ->
            MessageCard(message)
        }
    }
    
@Composable
fun  MessageCard(msg: Message) {
    Row(modifier = Modifier.padding(10.dp)
        .background(MaterialTheme.colorScheme.background)
    ) {
        Image(
            painter = painterResource(id = R.mipmap.ic_launcher),
            contentDescription = null,
            modifier = Modifier.size(80.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        // 是否是展开标识
        var isExpanded by remember { mutableStateOf(false) }
        // 颜色动画
        val surfaceColor: Color by animateColorAsState(
            targetValue = if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
        )
        Column(modifier = Modifier.clickable{ isExpanded = !isExpanded }) {
            Text(
                text = "Hello ${msg.author}",
                color = MaterialTheme.colorScheme.secondaryFixedDim)
            //添加一个间距
            Spacer(modifier = Modifier.height(20.dp))
            Surface(
                shape = MaterialTheme.shapes.medium, //圆角
                shadowElevation = 5.dp, // 阴影
                color = surfaceColor, // 颜色
                modifier = Modifier.animateContentSize().padding(1.dp) // 动画速度
            ) {
                Text(
                    text = "Hello ${msg.body}",
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = if(isExpanded) Int.MAX_VALUE else 1
                )
            }

        }
    }

}

```

### 点击变色区域

![image-20260806164813350](compose\image-20260806164813350.png)

``` kotlin
@Preview (showBackground = false)
@Composable
fun PhotographerCard(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.clip(RoundedCornerShape(5.dp))
            .background(color = MaterialTheme.colorScheme.surface) //.padding(16.dp) 变色区域小
            .clickable(onClick = {}).padding(16.dp)
    ) {
        Surface(
            modifier = Modifier.size(50.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
        ) { }
        Spacer(modifier = Modifier.width(20.dp))
        Column(
            modifier = Modifier.padding()

        ) {
            Text(text = "Alfreef Sisley", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(10.dp))
            CompositionLocalProvider(LocalContentColor provides LocalContentColor.current.copy()) {
                Text(text = "3 minutes ago", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }

}
```

### Slots API

![image-20260806165046349](compose\image-20260806165046349.png)

#### Scaffold

可以实现基本Material Design布局结构的界面，如TopAppBar，BottomAppBar，Floating ActionButton和Drawer

### 自定义View

![image-20260807112042881](compose\image-20260807112042881.png)

``` kotlin
fun Modifier.firstBaselineToTop(firstBaselineToTop: Dp) = this.then(
    layout { measurable, constraints ->
        // 测量元素
        val placeable = measurable.measure(constraints)
        // 获取元素的基线值
        val firstBaseline = placeable[FirstBaseline]
        // 元素新的Y坐标 = 新基线值 - 旧基线值
        val placeableY = firstBaselineToTop.roundToPx() - firstBaseline
        val height = placeable.height + placeableY
        layout(placeable.width , height) {
            placeable.placeRelative(0, placeableY)
        }
    }
)

@Preview(showBackground = false)
@Composable
fun TestWithPaddingToBaseline() {
    Text(text = "Hi there",
        Modifier.firstBaselineToTop(24.dp).background(Color.Red))

}

```

### 自定义Column

``` kotlin
/**
 * 自定义垂直排列的 Column 布局组件
 *
 * @param modifier 修饰符，用于调整布局的大小、位置或添加行为
 * @param content 可组合内容块，包含需要垂直排列的子组件
 */
@Composable
fun MyOwnColumn(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    // Layout composable 是创建自定义布局的核心 API
    // 它接收三个主要部分：modifier、content（子组件 lambda）以及 measurePolicy（测量策略 lambda）
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        
        // --- 第一阶段：测量 (Measure) ---
        // measurables:当前布局所有直接子组件的可测量对象列表
        // constraints: 父容器传递给当前布局的尺寸限制（最小/最大宽高）
        
        // 遍历所有子组件，并使用父容器传递的限制条件对它们进行测量
        // measure() 返回一个 Placeable 对象，其中包含了子组件测量后的实际尺寸
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }

        // --- 第二阶段：计算布局尺寸与位置 (Layout Calculation) ---
        // 初始化垂直方向的起始坐标 yPosition
        var yPosition = 0

        // layout() 函数用于确定当前自定义布局的最终尺寸，并提供放置子组件的作用域
        // 这里我们将布局的宽度设置为父容器允许的最大宽度，高度设置为父容器允许的最大高度
        // 注意：在实际生产中，通常需要根据 placeables 的实际总高度来计算布局高度，而不是直接使用 constraints.maxHeight
        layout(constraints.maxWidth, constraints.maxHeight) {
            
            // --- 第三阶段：放置 (Place) ---
            // 遍历所有已测量的子组件 (Placeable)
            placeables.forEach { placeable ->
                // placeRelative: 将子组件放置在指定坐标 (x, y)
                // x = 0: 所有子组件左对齐
                // y = yPosition: 子组件垂直堆叠，每个组件紧接在上一个组件下方
                placeable.placeRelative(x = 0, y = yPosition)
                
                // 更新下一个子组件的 y 坐标：当前 y 坐标 + 当前子组件的高度
                yPosition += placeable.height
            }
        }
    }
}
@Composable
fun MyOwnColumnSample() {
    MyOwnColumn {
        Text("MyOwnColumn")
        Text("places items")
        Text("vertically.")
        Text("We`ve don it by hand!")
    }
}

```

### 约束布局

``` kotlin
@Composable
fun ConstraintLayoutContent() {
    // ConstraintLayout 允许通过约束关系来定位子组件，类似于 XML 中的 ConstraintLayout
    ConstraintLayout {
        // 1. 创建引用 (References)
        // createRefs() 用于生成多个 ConstrainedLayoutReference 对象
        // 这里解构赋值创建了 button 和 text 两个引用，分别代表下方的 Button 和 Text 组件
        val (button, text) = createRefs()

        // 2. 定义 Button 组件及其约束
        Button(
            onClick = {},
            // constrainAs 修饰符将当前组件绑定到指定的引用 (button)
            modifier = Modifier.constrainAs(button) {
                // 约束规则：Button 的顶部连接到父容器 (parent) 的顶部
                // margin = 16.dp 表示两者之间保持 16dp 的间距
                top.linkTo(parent.top, margin = 16.dp)
            }
        ) {
            Text(text = "Button")
        }

        // 3. 定义 Text 组件及其约束
        Text(
            text = "Text",
            // 将当前 Text 组件绑定到 text 引用
            modifier = Modifier.constrainAs(text) {
                // 约束规则 1：Text 的顶部连接到 Button 的底部
                // 这确保了 Text 始终显示在 Button 下方，且间距为 16dp
                top.linkTo(button.bottom, margin = 16.dp)
                
                // 约束规则 2：Text 在水平方向上相对于父容器居中
                centerHorizontallyTo(parent)
            }
        )
    }
}
```

#### 关键知识点解析：

1. ‌**`createRefs()`**‌：
   - 必须在 `ConstraintLayout` 的作用域内调用。
   - 返回的引用（如 `button`, `text`）是连接 Compose 组件与约束规则的桥梁。
2. ‌**`Modifier.constrainAs(ref)`**‌：
   - 这是应用约束的核心修饰符。
   - `ref` 必须是通过 `createRefs()` 或 `createRef()` 创建的引用。
3. ‌**`linkTo` 与 `parent`**‌：
   - `top.linkTo(parent.top)`：将组件顶部对齐父容器顶部。
   - `top.linkTo(button.bottom)`：将组件顶部对齐另一个组件（button）的底部，实现垂直排列。
   - `parent` 是 `ConstraintLayout` 内置的特殊引用，代表布局容器本身。
4. ‌**`centerHorizontallyTo(parent)`**‌：
   - 快捷方式，等同于同时设置 `start.linkTo(parent.start)` 和 `end.linkTo(parent.end)`，实现水平居中。

``` kotlin
@Composable
fun ConstraintLayoutContent2() {
    ConstraintLayout {
        // 1. 创建引用
        // 为三个组件分别创建引用：button (左按钮), button2 (右按钮), text (中间文本)
        val (button, button2, text) = createRefs()

        // 2. 左侧按钮 (Button 1)
        Button(
            onClick = {},
            modifier = Modifier.constrainAs(button) {
                // 约束：顶部距离父容器顶部 16dp
                top.linkTo(parent.top, margin = 16.dp)
                // 注意：此处未设置水平约束，默认靠左对齐（取决于布局方向）
            }
        ) {
            Text(text = "Button 1")
        }

        // 3. 中间文本 (Text)
        Text(
            text = "Text",
            modifier = Modifier.constrainAs(text) {
                // 约束 1：顶部距离 button 底部 16dp (垂直排列在 button 下方)
                top.linkTo(button.bottom, margin = 16.dp)
                
                // 约束 2：水平方向上，以 button 的结束边（右侧）为中心进行对齐
                // 这通常用于让文本紧跟在按钮右侧或与其右边缘对齐
                centerAround(button.end)
            }
        )
        // 4. 创建屏障 (Barrier)
        // createEndBarrier 创建一个虚拟的垂直参考线，位于 button 和 text 两者最右侧边缘的外侧
        // 无论 button 还是 text 谁更宽/更靠右，barrier 都会自动调整到它们共同的最右端
        val barrier = createEndBarrier(button, text)
        // 5. 右侧按钮 (Button 2)
        Button(
            onClick = {},
            modifier = Modifier.constrainAs(button2) {
                // 约束 1：顶部距离父容器顶部 16dp (与 Button 1 顶部对齐)
                top.linkTo(parent.top, margin = 16.dp)
                
                // 约束 2：起始边（左侧）连接到 barrier
                // 这确保了 Button 2 始终位于 button 和 text 的右侧，且不会与它们重叠
                // 即使 button 或 text 的动态宽度发生变化，Button 2 也会自动保持间距
                start.linkTo(barrier)
            }
        ) {
            Text(text = "Button 2")
        }
    }
}
```

- *** 引用 **

  是使用createRefs()或createRefFor()创建，ConstraintLayout中的每个可组合项都 需要有与之关联的引用

- *** 约束条件 **

  使用用constrainAs()修饰符提供，该修饰符将引用作用参数，可以在lambde中指定其约束条件

- 约束条件是使用linkTo()或其他有用的方法指定的
- parent是一个现有的引用，可用于指定对于ConstrainLayout可以组合项本身的约束条件.。

### 自动换行

``` kotlin
@Preview(showBackground = true)
@Composable
fun LargeLayoutContent() {
    ConstraintLayout {
        // 【修复1】使用 createRef() 创建单个引用
        val text = createRef()
        // 创建一条位于父容器宽度 50% 处的垂直参考线
        val guideline = createGuidelineFromStart(fraction = 0.5f)

        Text(
            text = "Text long Text long Text long Text long Text long Text long Text long Text long Text long Text long Text long",
            modifier = Modifier.constrainAs(text) {
                // 约束：起始边连接到参考线，结束边连接到父容器右侧
                linkTo(start = guideline, end = parent.end)
                width = Dimension.preferredWrapContent // 自动换行
            }
        )
    }
}
```

### 解耦，根据屏幕旋转修改属性

``` kotlin
/**
 * 创建解耦的约束集
 *
 * @param margin 动态传入的间距值，用于控制组件间的垂直距离
 * @return ConstraintSet 定义了一组完整的约束规则
 */
private fun decoupledConstraints(margin: Dp): ConstraintSet {
    // 构建一个 ConstraintSet 对象
    return ConstraintSet {
        // 通过 layoutId 字符串创建引用
        // 这些 ID 必须与 UI 组件中 Modifier.layoutId(...) 指定的字符串完全一致
        val button = createRefFor("button")
        val text = createRefFor("text")
        // 定义 button 的约束
        constrain(button) {
            // 顶部连接到父容器顶部，使用动态传入的 margin
            top.linkTo(parent.top, margin)
        }
        // 定义 text 的约束
        constrain(text) {
            // 顶部连接到 button 的底部，使用相同的动态 margin
            // 这实现了垂直排列：Parent Top -> Button -> Text
            top.linkTo(button.bottom, margin)
        }
    }
}
/**
 * 解耦约束布局示例
 *
 * 核心思想：
 * 1. 约束逻辑与 UI 组件分离：UI 组件只负责声明自己的 layoutId，不直接包含约束代码。
 * 2. 动态约束集：根据父容器的尺寸（宽高比），动态选择使用哪一套约束规则。
 */
@Preview(showBackground = true)
@Composable
fun DecoupledConstraintLayout2() {
    // BoxWithConstraints 允许我们在组合阶段获取父容器的最大宽高限制
    BoxWithConstraints {
        // 根据容器的宽高比决定使用哪种约束策略
        // 如果宽度小于高度（竖屏或窄布局），使用小间距约束
        // 否则（横屏或宽布局），使用大间距约束
        val constraint = if (maxWidth < maxHeight) {
            decoupledConstraints(16.dp)
        } else {
            decoupledConstraints(160.dp)
        }
        // 将计算好的 ConstraintSet 传递给 ConstraintLayout
        // 此时，ConstraintLayout 内部不再需要 defineConstraints lambda，而是直接使用传入的 constraint
        ConstraintLayout(constraint) {
            // 按钮组件：仅通过 layoutId 标识自己，不包含任何约束逻辑
            Button(
                onClick = {},
                modifier = Modifier.layoutId("button")
            ) {
                Text(text = "Button")
            }

            // 文本组件：仅通过 layoutId 标识自己
            Text(
                text = "Text",
                modifier = Modifier.layoutId("text")
            )
        }
    }
}
```

### Intrinsics

``` kotlin
@Preview(showBackground = true)
@Composable
fun TwoTexts(modifier: Modifier = Modifier) {
    Row(modifier = modifier.height(IntrinsicSize.Min)) { //调试和内容一致
        Text(
            text = "Hi",
            modifier = Modifier
                .padding(4.dp)
                .weight(1f)
                .wrapContentWidth(Alignment.Start),
        )
        Divider(color = Color.Black, modifier = Modifier
            .fillMaxHeight()
            .width(1.dp))
        Text(
            text = "there",
            modifier = Modifier.weight(1f).padding(4.dp).wrapContentWidth(Alignment.End)
        )
    }
}
```























