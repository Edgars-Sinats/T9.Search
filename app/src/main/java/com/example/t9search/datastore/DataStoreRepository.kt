package com.example.t9search.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.t9search.AppConstants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class DataStoreRepository(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("t9_pref")
        val WORD_MAXIMUM_KEY = intPreferencesKey("word_maximum_key")
        val SHOW_POP_UP_KEY = booleanPreferencesKey("pop_up_disabled")

    }

    val readMaxWordCountState: Flow<Int> =
        context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map {
                it[WORD_MAXIMUM_KEY] ?: AppConstants.WORDS_IN_DISPLAY_COUNT
            }

    suspend fun saveMaxWordCountState(count: Int) {
        context.dataStore.edit {
            it[WORD_MAXIMUM_KEY] = count
        }
    }

    val readIsPopUpMsgOn: Flow<Boolean> =
        context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map {
                it[SHOW_POP_UP_KEY] ?: false
            }
    suspend fun saveIsPopUpMsgOn(exist: Boolean){
        context.dataStore.edit {
            it[SHOW_POP_UP_KEY] = exist
        }
    }

}