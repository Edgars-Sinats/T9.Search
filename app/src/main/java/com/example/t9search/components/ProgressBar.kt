package com.example.t9search.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.t9search.AppConstants

@Composable
fun ProgressBar() {
    Box (
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = AppConstants.LOADING_DATA)
            Spacer(modifier = Modifier.padding(8.dp))

            CircularProgressIndicator(
                modifier = Modifier.size(60.dp),
            )
        }
    }
}

@Composable
@Preview
fun ProgressBarPreview(){
    ProgressBar()
}