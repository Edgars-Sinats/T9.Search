package com.example.t9search.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    onOpen: () -> Unit
) {
    var openMenu by remember { mutableStateOf(false) }

    MediumTopAppBar(
        title = {
            Text(text = "T9 Word Search")
        },
        actions = {
            IconButton(onClick = { openMenu =!openMenu }) {
                Icon(
                    imageVector = Icons.Outlined.MoreVert,
                    contentDescription = null
                )
            }
        }
    )
}