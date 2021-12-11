package com.onirutla.githubuser.util

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

val Context.introDataStore by preferencesDataStore(Constant.INTRO_DATASTORE)

@Singleton
class DataStoreManager @Inject constructor(context: Context) {

    private object PreferencesKeys {
        val IS_SEEN = booleanPreferencesKey(Constant.KEY_INTRO_PREFERENCES)
    }

    private val introDataStore = context.introDataStore

    val preferencesIsSeen = introDataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map {
        it[PreferencesKeys.IS_SEEN] ?: false
    }

    suspend fun updateIsSeen() {
        introDataStore.edit {
            it[PreferencesKeys.IS_SEEN] = !(it[PreferencesKeys.IS_SEEN] ?: false)
        }
    }
}
