package com.example.t9search.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.t9search.feature_search.SearchEvent

@Composable
fun SettingsScreen(
    loadedWordCount: Int,
    onLoadedWordCountChange: (SearchEvent) -> Unit,
    switchPopUpSettings: Boolean,
    onSwitchPopUpChange: (Boolean) -> Unit,

) {
     var newCount by remember { mutableStateOf(loadedWordCount) }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .align(CenterHorizontally)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Show pop up",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(0.7f)
                )

                Switch(
                    checked = switchPopUpSettings,
                    onCheckedChange = onSwitchPopUpChange,
                    modifier = Modifier.weight(0.3f)
                )
//                Switch(currentCompositionLocalContext, isDarkTheme)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Loaded word count:",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(0.7f)
                )

                TextField(
                    value = newCount.toString(),
                    onValueChange = { newValue ->
//                        val count = newValue.toIntOrNull() ?: 0
                        newCount = newValue.toIntOrNull() ?: 0
//                        onLoadedWordCountChange(count)
                    },
                    modifier = Modifier.weight(0.3f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),

                    )
            }

            FilledIconButton(
                modifier = Modifier
                    .align(CenterHorizontally)
                    .padding(20.dp),
                enabled = newCount!=0 && newCount != loadedWordCount,
                onClick = {
                    Log.i("SettingsScreen","onClick: $newCount")
                    onLoadedWordCountChange(SearchEvent.UpdateWordCount(newCount))
                          },

            ) {
                Text(text = "Save changes")
            }
        }
    }
}

@Composable
@Preview
fun SettingsPreview(){
    SettingsScreen(
        loadedWordCount = 20,
        onLoadedWordCountChange = {},
        switchPopUpSettings = false,
        onSwitchPopUpChange = {}
    )
}
