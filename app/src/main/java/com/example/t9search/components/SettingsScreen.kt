package com.example.t9search.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(
    loadedWordCount: Int,
    onLoadedWordCountChange: (Int) -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Theme:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

//                Switch(
//                    checked = isDarkTheme,
//                    onCheckedChange = onThemeToggle
//                )
//                Switch(currentCompositionLocalContext, isDarkTheme)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Loaded word count:",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )

                BasicTextField(
                    value = loadedWordCount.toString(),
                    onValueChange = { newValue ->
                        val count = newValue.toIntOrNull() ?: 0
                        onLoadedWordCountChange(count)
                    },
                    modifier = Modifier.width(100.dp)
                )
            }
        }
    }
}
