package com.example.t9search.feature_search


//TODO make
sealed class SearchEvent {
    data class Lookup(val lookUpValue: Long): SearchEvent()
    data class UpdateWordCount(val count: Int): SearchEvent()
    data class UpdatePopUpSettings(val popUp: Boolean): SearchEvent()
    data class SetDialogMessage(val dialogMsg: String?): SearchEvent()
}