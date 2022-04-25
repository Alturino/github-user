package com.onirutla.githubuser.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onirutla.githubuser.data.Resource
import com.onirutla.githubuser.data.source.UserDataSource
import com.onirutla.githubuser.data.source.local.entity.UserEntity
import com.onirutla.githubuser.util.MainDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class DetailViewModel @Inject constructor(
    private val userDataSource: UserDataSource,
    @MainDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _username = MutableSharedFlow<String>()

    val user: StateFlow<Resource<UserEntity>> = _username.flatMapLatest {
        userDataSource.getUserDetail(it)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = Resource.Loading()
    )

    fun setFavorite(userEntity: UserEntity) {
        viewModelScope.launch {
            userDataSource.setUserFavorite(userEntity)
        }
    }

    fun getUser(username: String) {
        viewModelScope.launch(dispatcher) {
            _username.emit(username)
        }
    }
}
