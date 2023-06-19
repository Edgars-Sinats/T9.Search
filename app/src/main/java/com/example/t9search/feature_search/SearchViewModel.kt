package com.example.t9search.feature_search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.t9search.AppConstants.WORDS_IN_DISPLAY_COUNT
import com.example.t9search.DataRepository
import com.example.t9search.feature_search.state.SearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.collections.HashSet


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val dataRepository: DataRepository
): ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state.asStateFlow()
    private lateinit var allWords: HashSet<String>
    //TODO add Class SearchEvent

    fun loadDictionary(){
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {

            val dictionary = dataRepository.loadDictionary()
            allWords = HashSet(dictionary)

            //When we have allWords loading is finished and on Screen words are shown.
            if (allWords.isNotEmpty() ){
                _state.value = _state.value.copy(isSuccess = true)
            }

            //Stop the progress bar for Screen
            _state.value = _state.value.copy(isLoading = false)
            lookup("")
        }
    }

    /**
     * Lookup fun is executed on every time user change search bar.
     * [digits] represent numbers in string format and "" in case of null in search bar, then returns words without filter.
     *
     * return is list of listOfSearchedWords but for Screen, it take [_state] instead, so it is redundant and can be removed.
     */
    fun lookup(digits: String){         //: List<String> {

        var listOfSearchedWords: List<String> = emptyList()

        //Case when search bar is empty
        if (digits=="") {
            listOfSearchedWords = allWords.take(WORDS_IN_DISPLAY_COUNT)
            _state.value = _state.value.copy(searchedWords = listOfSearchedWords, digits = null)

        //Case when search bar has numbers
        }else{
            val searchableDigits = convertNumberToLetters(digits)
            Log.i("searchableDigits", "searchableDigits: $searchableDigits")


            // T9 custom Filtering
            viewModelScope.launch {
                listOfSearchedWords = withContext(Dispatchers.Main){
                    allWords.filter { string ->
                        searchableDigits.any {
                                start -> string.startsWith(start)
                        }
                    }.take(WORDS_IN_DISPLAY_COUNT) //TODO add from settings/dataStore
                }

            }

            //*1 My approach with state
            _state.value = _state.value.copy(searchedWords = listOfSearchedWords, digits = digits.toInt())
        }

        //*1 Requirements
        //return listOfSearchedWords
    }
    fun setDialog(text: String?){
        if (text==null){
            _state.value = _state.value.copy(dialogText = null)
        }else{
            _state.value = _state.value.copy(dialogText = text)
        }
    }
}

/**
 * [T9](https://en.wikipedia.org/wiki/T9_(predictive_text)) number converter
 */
fun convertNumberToLetters(number: String): List<String> {
    val digitLettersMap = mapOf(
        '2' to listOf('a', 'b', 'c'),
        '3' to listOf('d', 'e', 'f'),
        '4' to listOf('g', 'h', 'i'),
        '5' to listOf('j', 'k', 'l'),
        '6' to listOf('m', 'n', 'o'),
        '7' to listOf('p', 'q', 'r', 's'),
        '8' to listOf('t', 'u', 'v'),
        '9' to listOf('w', 'x', 'y', 'z')
    )
    val results = mutableListOf<String>()

    fun generateCombinations(current: String, digits: String) {
        if (digits.isEmpty()) {
            results.add(current)
            return
        }

        val digit = digits[0]
        val letters = digitLettersMap[digit] ?: emptyList()

        for (letter in letters) {
            generateCombinations(current + letter, digits.substring(1))
        }
    }

    if (number.isNotEmpty()) {
        generateCombinations("", number)
    }

    return results
}