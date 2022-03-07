package com.onirutla.githubuser.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.onirutla.githubuser.data.preference.UserDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userDataStore: UserDataStore
) : ViewModel() {

    val isDarkTheme = userDataStore.isDarkTheme.asLiveData(viewModelScope.coroutineContext)

    fun setIsDarkTheme(isDarkTheme: Boolean) {
        viewModelScope.launch {
            userDataStore.setIsDarkTheme(isDarkTheme)
        }
    }

}
