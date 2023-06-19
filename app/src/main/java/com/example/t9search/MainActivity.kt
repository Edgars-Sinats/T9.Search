package com.example.t9search

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.t9search.feature_search.SearchScreen
import com.example.t9search.ui.theme.T9SearchTheme
import dagger.hilt.android.AndroidEntryPoint

//@HiltAndroidApp
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            T9SearchTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SearchScreen()
                }

            }
        }
    }
}

@Composable
fun SettingsPanel() {
    var isOpen by remember { mutableStateOf(false) }

    val slideOffset = animateFloatAsState(
        targetValue = if (isOpen) 0f else -300f,
        animationSpec = tween(durationMillis = 300, easing = CubicBezierEasing(0.4f, 0f, 0.2f, 1f))
    ).value

    Surface(
        modifier = Modifier
            .fillMaxHeight()
            .width(300.dp)
            .offset { IntOffset(slideOffset.toInt(), 0) }
            .background(Color.White),
        shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
    ) {
        SearchScreen()
    }

    IconButton(
        onClick = { isOpen = !isOpen },
        modifier = Modifier.padding(8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = "Settings"
        )
    }
}