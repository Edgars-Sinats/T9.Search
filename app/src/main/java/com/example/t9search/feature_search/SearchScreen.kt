package com.example.t9search.feature_search

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.t9search.AppConstants
import com.example.t9search.AppConstants.ENTER_NUMBERS
import com.example.t9search.components.ProgressBar
//import androidx.lifecycle.compose.collectAsStateWithLifecycle


@Composable
fun SearchScreen(searchViewModel: SearchViewModel){
//    val searchViewModel: SearchViewModel = viewModel()
    LaunchedEffect(key1 = true){
        searchViewModel.loadDictionary()
    }

    val searchState by searchViewModel.state.collectAsState()
    SearchBar(
        text = searchState.digits.toString(),
        onValueChange = {
            Log.i("Screen","onValueChange digits: ${searchState.digits}")
            Log.i("Screen","onValueChange it: $it")
            if (it == "1"|| it == "0"){
                searchViewModel.setDialog(AppConstants.ENTER_1_OR_2)
            }
            if ( it != "null" ){
                searchViewModel.lookup( it )
            }
        },
        enabled = searchState.isSuccess,
        modifier = Modifier,
        chosenWords = searchState.searchedWords,
        popUpMsg = searchState.dialogText,
        showDialogMsg = {searchViewModel.setDialog(text = it)}
    )
}

@Composable
fun SearchBar(
    text: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean,
    modifier: Modifier,
    chosenWords: List<String>?,
    popUpMsg: String?,
    showDialogMsg:(String?) ->Unit
){


    if (popUpMsg!=null){
        MyDialog(
            text = popUpMsg,
            onDismiss = { showDialogMsg(null) }
        )
    }

    Scaffold(
        bottomBar = {
            BasicTextField(
                value = if ( text!="null" ) {
                    text
                } else { ("") },
                onValueChange = {
                    if (it.isBlank() || (it.toIntOrNull() != null && !it.endsWith("1") && !it.endsWith("0") ) ){
                        onValueChange(it)
                    } else {
                        showDialogMsg(AppConstants.ENTER_VALID_NUMBER)
                        Log.i("","Sorry unacceptable parameter")
                    }
                },
                enabled = enabled,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                textStyle = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.DarkGray
                ),
                modifier = modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(4.dp)
//                    .align(CenterHorizontally)
                    .fillMaxWidth(),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 64.dp) // margin left and right
                            .fillMaxWidth()
                            .border(
                                width = 2.dp,
                                color = Color(0xFFAAE9E6),
                                shape = RoundedCornerShape(size = 16.dp)
                            )
                            .padding(horizontal = 16.dp, vertical = 12.dp), // inner padding
                    ) {
                        if (text.isEmpty() || text == "" || text == "null") {
                            Text(
                                text = ENTER_NUMBERS,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        innerTextField()
                    }

                }
            )
        },
        content = {
            if (!enabled){
                ProgressBar()
            } else {
                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn {
                        item {
                            chosenWords?.forEach { wordItem ->
                                val annotatedString = buildAnnotatedString {
                                    append(wordItem)
                                    if (text != "null"){
                                        addStyle(
                                            SpanStyle(fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = Color.Red),
                                            start = 0,
                                            end = text.length)
                                    }
                                }
                                Text(text = annotatedString,
                                    modifier
                                        .padding(start = 16.dp,
                                            end = 16.dp,
                                            top = 8.dp,
                                            bottom = 0.dp)
                                        .fillMaxWidth())
                            }
                        }
                    }
                }
            }
        }
    )

}

@Composable
fun MyDialog(
    text: String,
    onDismiss: () ->Unit
){
    AlertDialog(
        onDismissRequest = { onDismiss.invoke() },
        text = { Text(text = text) },
        shape = RoundedCornerShape(32.dp),
        title = { Text(text = AppConstants.SUGGESTION) },
        
        confirmButton = {
            TextButton(onClick = onDismiss){ Text(text = "Ok")}
         },

    )
}

@Preview
@Composable
fun SearchBarPreview(){
    SearchBar(
        text = "42",
        onValueChange = {},
        enabled = false,
        modifier = Modifier.padding(16.dp),
        chosenWords = listOf("as","bs","as","Arcade","Arcade","boolean", "algorithm","Brazil","Maya","Waterfall","Bud","Speakers", "bluetooth", "performance"),
        showDialogMsg = {},
        popUpMsg = null
    )
}
