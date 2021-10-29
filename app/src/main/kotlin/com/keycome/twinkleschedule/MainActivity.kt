package com.keycome.twinkleschedule

import android.content.Intent
import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keycome.twinkleschedule.databinding.ActivityMainBinding
import com.keycome.twinkleschedule.presentation.display.DisplayActivity

class MainActivity : BaseActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//         binding = ActivityMainBinding.inflate(layoutInflater)
//         setContentView(binding.root)
//         setContent {
//             MainContent {
//                startActivity(Intent(this, DisplayActivity::class.kotlin))
//            }
//        }
        startActivity(Intent(this, DisplayActivity::class.java))
        finish()
    }
}

@Composable
fun MainContent(buttonEvent: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "twinkle",
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(color = Color.Black)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text("hello, compose")
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = buttonEvent,
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier
                .height(36.dp)
                .width(70.dp)
        ) {
            Text(
                text = "确认",
                fontSize = 14.sp
            )
        }
    }
}

@Preview
@Composable
fun CompletePreview() {
}