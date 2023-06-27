package com.example.t9search.feature_search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.t9search.AppConstants
import com.example.t9search.DataRepository
import com.example.t9search.datastore.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val dataStoreRepository: DataStoreRepository
): ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state.asStateFlow()

    private val trie = Trie()
    //TODO implement Class SearchEvents

    fun loadDictionary(){
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            val words = dataRepository.loadDictionary()

            for (word in words) {
                trie.addWord(word)
            }

            lookup("")
            _state.value = _state.value.copy(isLoading = false)
            //To show ProgressBar
            delay(500)
            //When we have allWords loading is finished and on Screen words are shown.
            if (trie.checkWord("abiogenetically") ){
                _state.value = _state.value.copy(isSuccess = true)
            }

        }

    }

    //Digits are prefix what are searched in trie

    /**
     * Lookup fun is executed on every time user change search bar.
     * [digits] represent numbers in string format and "" in case of null in search bar, then returns words without filter.
     *
     * return is list of listOfSearchedWords but for Screen, it take [_state] instead, so it is redundant and can be removed.
     */
    fun lookup(digits: String){

        val convertedNumberChar = convertNumberToLetters(digits)
        val results1 = trie.searchWordsWithPrefix(convertedNumberChar, _state.value.maxWordsCount)

        if (convertedNumberChar == null){
            _state.value = _state.value.copy(searchedWords = results1, digits = null)
        } else {
            _state.value = _state.value.copy(searchedWords = results1, digits = digits.toInt())
        }
    }
    fun setDialog(text: String?){
        if (text==null){
            _state.value = _state.value.copy(dialogText = null)
        }else{
            _state.value = _state.value.copy(dialogText = text)
        }
    }



    //Func. to update settings and toggle pop up msg dialog.
    fun savePopUpSettings(popUpSettings: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveIsPopUpMsgOn(popUpSettings)
            Log.i(AppConstants.TAG_SEARCH_VIEW_MODEL, "popUpSettings: $popUpSettings")
        }
    }

    fun updatePopUpSettings() {
        viewModelScope.launch(Dispatchers.IO) {
            val popUpDialog= dataStoreRepository.readIsPopUpMsgOn.first()
            _state.value = _state.value.copy(dialogPopUp = popUpDialog)
        }
    }

    fun saveMaxWordCount(wordCount: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveMaxWordCountState(wordCount)
            Log.i(AppConstants.TAG_SEARCH_VIEW_MODEL, "saveMaxWordCountState: $wordCount")
            updateMaxWordCount()
        }
    }

    /** Updating in DataStore maxWords what are set as defaults in WORDS_IN_DISPLAY_COUNT
     */
    fun updateMaxWordCount() {
        viewModelScope.launch(Dispatchers.IO) {
            val maxWords= dataStoreRepository.readMaxWordCountState.first()
            _state.value = _state.value.copy(maxWordsCount = maxWords)
        }
    }
}

/**
 * [T9](https://en.wikipedia.org/wiki/T9_(predictive_text)) number converter
 */
fun convertNumberToLetters(number: String): List<String>? {
    if (number == ""){
        return null
    }
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
