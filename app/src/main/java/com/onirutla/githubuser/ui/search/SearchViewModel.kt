package com.onirutla.githubuser.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onirutla.githubuser.data.Resource
import com.onirutla.githubuser.data.UserDTO
import com.onirutla.githubuser.data.source.UserDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val userDataSource: UserDataSource
) : ViewModel() {

    private val _username = MutableSharedFlow<String>()

    val users: StateFlow<Resource<List<UserDTO>>> = _username.flatMapLatest {
        userDataSource.getUsersSearch(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Resource.Loading()
    )

    fun search(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _username.emit(username)
        }
    }

}