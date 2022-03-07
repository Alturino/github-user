package com.onirutla.githubuser.ui

import androidx.lifecycle.*
import com.onirutla.githubuser.data.preference.UserDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val userDataStore: UserDataStore
) : ViewModel() {

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> get() = _username

    fun setUsername(username: String) {
        _username.value = username
    }

    val isDarkTheme = userDataStore.isDarkTheme.asLiveData(viewModelScope.coroutineContext)

}
