@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.t9search

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.t9search.components.SettingsScreen
import com.example.t9search.feature_search.SearchScreen
import com.example.t9search.feature_search.SearchViewModel
import com.example.t9search.ui.theme.T9SearchTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            T9SearchTheme {
                AppContent()
            }
        }
    }
}

@Composable
fun AppContent() {
    val drawerState = remember { mutableStateOf(DrawerValue.Open) }
    val searchViewModel: SearchViewModel = viewModel()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "${R.string.app_name} - ${searchViewModel.state.value.maxWordsCount} ${AppConstants.MAX_WORDS}") },
                navigationIcon = {
                    IconButton(onClick = {
                        if(drawerState.value ==DrawerValue.Open) {
                            drawerState.value=DrawerValue.Closed
                        } else {
                            drawerState.value = DrawerValue.Open
                        }
                    }
                    ){
                        Icon(imageVector = Icons.Default.Settings, contentDescription = null)
                    }
                }
            )
        },
        content = { paddingValue->

            BoxWithConstraints(modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(paddingValue)
            ) {

                val parentWith = this.maxWidth
                val parentHeight = this.maxHeight
                val slideOffset = animateFloatAsState(
                    targetValue = (if (drawerState.value != DrawerValue.Open) 0f else (-parentWith.value / 1.3).toFloat()),
                    animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                )

                //Settings panel
                Box {
                    Box(modifier = Modifier.offset(
                        x = slideOffset.value.dp,
                        y = 0.dp
                    )) {

                    }
                    SearchScreen(searchViewModel)

                    // Your settings content here
                    if(drawerState.value != DrawerValue.Open){
                        Box(modifier = Modifier
                            .fillMaxHeight()
                            .size(width = parentWith, height = parentHeight)
                            .offset { IntOffset(x = slideOffset.value.toInt(), y = 0) }
                            .background(Color.LightGray)
                        ){

                            SettingsScreen(
                                loadedWordCount = searchViewModel.state.value.maxWordsCount,
                                onLoadedWordCountChange = { searchViewModel.saveMaxWordCount(it) },
                                onSwitchPopUpChange = { searchViewModel.savePopUpSettings(it) },
                                switchPopUpSettings = searchViewModel.state.value.dialogPopUp,
                            )
                        }

                    }
                }
            }
        }

    )
}

@Preview
@Composable
fun MainPreview(){
    AppContent()
}