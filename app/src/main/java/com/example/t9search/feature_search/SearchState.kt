package com.example.t9search.feature_search
import com.example.t9search.AppConstants.WORDS_IN_DISPLAY_COUNT

/**
 * State of screen.
 * [isLoading] state true, when words.list in loading first time when app is opened. When false - Progress bar is displayed as loading
 * [digits] are user input and are converted to String and back.
 *      Check why in lookup need to use String and not Int.
 * [searchedWords] filtered words to display in Screen with limit of @ [WORDS_IN_DISPLAY_COUNT]
 *
 * [dialogText] show text in Suggestion Dialog. TODO add for dialog button - 'don`t show' and save it in DataStore custom text to not repeat for user.
 * [dialogPopUp] enabled to toggle dialogText dialog.
 */
data class SearchState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isFailure: Boolean = false,

    val dialogText: String? = null,
    val dialogPopUp: Boolean = true,
    var digits: Long? = null,
    val searchedWords: List<String> = emptyList(),
    val maxWordsCount: Int = WORDS_IN_DISPLAY_COUNT
)
