package com.onirutla.githubuser.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.onirutla.githubuser.core.data.UIState
import com.onirutla.githubuser.core.domain.data.User
import com.onirutla.githubuser.core.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _username = MutableLiveData<String>()

    val user: LiveData<UIState<User>> = _username.switchMap {
        userRepository.getDetailBy(it).asLiveData(viewModelScope.coroutineContext)
    }

    fun setFavorite(user: User) {
        viewModelScope.launch {
            userRepository.setFavorite(user)
        }
    }

    fun getUser(username: String) {
        _username.value = username
    }
}
