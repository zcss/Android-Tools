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





























