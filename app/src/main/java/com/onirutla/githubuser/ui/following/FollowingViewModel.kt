package com.onirutla.githubuser.ui.following

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onirutla.githubuser.data.Resource
import com.onirutla.githubuser.data.UserDTO
import com.onirutla.githubuser.data.source.UserDataSource
import com.onirutla.githubuser.util.MainDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class FollowingViewModel @Inject constructor(
    private val userDataSource: UserDataSource,
    @MainDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _username = MutableSharedFlow<String>()

    val user: StateFlow<Resource<List<UserDTO>>> = _username.flatMapLatest {
        userDataSource.getUsersFollowing(it)
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Resource.Loading()
    )

    fun getUser(username: String) {
        viewModelScope.launch(dispatcher) {
            _username.emit(username)
        }
    }

}
