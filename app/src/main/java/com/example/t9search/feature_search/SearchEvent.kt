package com.example.t9search.feature_search


//TODO make
sealed class SearchEvent {
    object Search: SearchEvent()
    object Lookup: SearchEvent()
    data class TypedString(val value: Long): SearchEvent()
}