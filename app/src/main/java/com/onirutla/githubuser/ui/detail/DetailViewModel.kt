package com.onirutla.githubuser.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.onirutla.githubuser.data.UIState
import com.onirutla.githubuser.data.repository.UserRepository
import com.onirutla.githubuser.data.source.local.entity.UserEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _username = MutableLiveData<String>()

    val user: LiveData<UIState<UserEntity>> = _username.switchMap {
        userRepository.getDetailBy(it).asLiveData(viewModelScope.coroutineContext)
    }

    fun setFavorite(userEntity: UserEntity) {
        viewModelScope.launch {
            userRepository.setFavorite(userEntity)
        }
    }

    fun getUser(username: String) {
        _username.value = username
    }
}
