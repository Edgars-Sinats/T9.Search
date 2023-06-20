package com.example.t9search.feature_search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.t9search.AppConstants
import com.example.t9search.AppConstants.TAG_SEARCH_VIEW_MODEL
import com.example.t9search.DataRepository
import com.example.t9search.feature_search.state.SearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.HashSet


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val dataRepository: DataRepository
): ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state.asStateFlow()
    private lateinit var allWords: HashSet<String>
    private lateinit var allWordsTrie: TrieNode
    //root of Trie
    private val allWordsTrie1: TrieNode = TrieNode()
    private val trie = Trie()

    //TODO add Class SearchEvent

    fun loadDictionary(){
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            Log.i(TAG_SEARCH_VIEW_MODEL, "Starting loadDic")
            val words = dataRepository.loadDictionary()
            Log.i(TAG_SEARCH_VIEW_MODEL, "loadDic is finished")


            for (word in words) {
                trie.addWord(word)
            }

            Log.i(TAG_SEARCH_VIEW_MODEL, "initial lookup.. ")
            lookup("")
            _state.value = _state.value.copy(isLoading = false)
            //When we have allWords loading is finished and on Screen words are shown.
            if (trie.checkWord("abiogenetically") ){
                Log.i(TAG_SEARCH_VIEW_MODEL, "abiogenetically found")
                _state.value = _state.value.copy(isSuccess = true)
            }

        }

    }

//    private fun addWord(word: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            var node = allWordsTrie1
//            for (char in word) {
//                val child = node.children[char] ?: TrieNode()
//                node.children[char] = child
//                node = child
//            }
//            node.isWord = true
//
//        }
//
//    }

    //Digits are prefix what are searched in trie
    fun lookup(digits: String){
        Log.i("ViewModel", "lookup: $digits")

//        val results = mutableListOf<String>()
        val convertedNumberChar = convertNumberToLetters(digits)
        println("ViewModel - convertedNumberChar: $convertedNumberChar" )
        val results1 = trie.searchWordsWithPrefix(convertedNumberChar, AppConstants.WORDS_IN_DISPLAY_COUNT)

        if (convertedNumberChar == null){
            _state.value = _state.value.copy(searchedWords = results1, digits = null)
            Log.i(TAG_SEARCH_VIEW_MODEL, "convertedNumberChar digits null")

        } else {
//            Log.i("TAG", "results: $results \n allWords: haplessness ${allWords.filter { s -> s=="haplessness" }}")
            _state.value = _state.value.copy(searchedWords = results1, digits = digits.toInt())
//            Log.i(TAG_SEARCH_VIEW_MODEL, "convertedNumberChar \n digits: $digits \n First 3 searchedWords: ${results1.subList(0,3)?: ""}")

        }
//        return results
    }

    private fun searchWordsFromNode(
        node: TrieNode,
        prefix: String,
        results: MutableList<String>,
        limit: Int
    ) {
        if (results.size >= limit) return
        if (node.isWord) {
            results.add(prefix)
        }
        for ((char, child) in node.children) {
            val newPrefix = prefix + char
            searchWordsFromNode(child, newPrefix, results, limit)
        }
    }
    private fun searchAllWordsFromNode(
        node: TrieNode,
        prefix: String,
        results: MutableList<String>,
        limit: Int
    ) {
        if (results.size >= limit) return
        if (node.isWord) {
            results.add(prefix)
        }
        for ((char, child) in node.children) {
            val newPrefix = prefix + char
            searchAllWordsFromNode(child, newPrefix, results, limit)
        }
    }

//    fun loadDictionaryOld(){
//        _state.value = _state.value.copy(isLoading = true)
//        viewModelScope.launch(Dispatchers.IO) {
//
//            val dictionary = dataRepository.loadDictionary()
//            allWords = HashSet(dictionary)
//            allWordsTrie = TrieNode()
//
//            //When we have allWords loading is finished and on Screen words are shown.
//            if (allWords.isNotEmpty() ){
//                _state.value = _state.value.copy(isSuccess = true)
//            }
//
//            //Stop the progress bar for Screen
//            _state.value = _state.value.copy(isLoading = false)
//            lookup("")
//        }
//    }

    /**
     * Lookup fun is executed on every time user change search bar.
     * [digits] represent numbers in string format and "" in case of null in search bar, then returns words without filter.
     *
     * return is list of listOfSearchedWords but for Screen, it take [_state] instead, so it is redundant and can be removed.
     */
//    fun lookup(digits: String){         //: List<String> {
//
//        var listOfSearchedWords: List<String> = emptyList()
//
//        //Case when search bar is empty
//        if (digits=="") {
//            listOfSearchedWords = allWords.take(WORDS_IN_DISPLAY_COUNT)
//            _state.value = _state.value.copy(searchedWords = listOfSearchedWords, digits = null)
//
//        //Case when search bar has numbers
//        }else{
//            val searchableDigits = convertNumberToLetters(digits)
//            Log.i("searchableDigits", "searchableDigits: $searchableDigits")
//
//
//            // T9 custom Filtering
//            viewModelScope.launch {
//                listOfSearchedWords = withContext(Dispatchers.Main){
//                    allWords.filter { string ->
//                        searchableDigits.any {
//                                start -> string.startsWith(start)
//                        }
//                    }.take(WORDS_IN_DISPLAY_COUNT) //TODO add from settings/dataStore
//                }
//
//            }
//
//            //*1 My approach with state
//            _state.value = _state.value.copy(searchedWords = listOfSearchedWords, digits = digits.toInt())
//        }
//
//        //*1 Requirements
//        //return listOfSearchedWords
//    }

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
