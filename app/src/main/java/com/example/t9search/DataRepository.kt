package com.example.t9search

import android.content.Context

class DataRepository(private val context: Context) {
    fun loadDictionary(): List<String> {
        val inputStream = context.assets.open(AppConstants.FILE_NAME)
        val lines = mutableListOf<String>()

        try {
            inputStream.bufferedReader().useLines { lines.addAll(it) }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            inputStream.close()
        }

        return lines
    }
}