package com.onirutla.githubuser.data.preference

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.onirutla.githubuser.util.KEY_IS_DARK_THEME
import com.onirutla.githubuser.util.USER_DATASTORE
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

val Context.userDataStore by preferencesDataStore(USER_DATASTORE)

@Singleton
class UserDataStore @Inject constructor(
    private val context: Context
) {

    private object PreferencesKeys {
        val IS_DARK_THEME = booleanPreferencesKey(KEY_IS_DARK_THEME)
    }

    private val userDataStore = context.userDataStore

    val isDarkTheme = userDataStore.data.catch { exception ->
        if (exception is IOException)
            emit(emptyPreferences())
        else
            throw exception
    }.map { it[PreferencesKeys.IS_DARK_THEME] ?: false }

    suspend fun setIsDarkTheme(isDarkTheme: Boolean) {
        userDataStore.edit {
            it[PreferencesKeys.IS_DARK_THEME] = isDarkTheme
        }
    }


}
