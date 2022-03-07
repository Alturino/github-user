package com.onirutla.githubuser.ui.detail

import androidx.lifecycle.*
import com.onirutla.githubuser.data.Resource
import com.onirutla.githubuser.data.local.entity.UserEntity
import com.onirutla.githubuser.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val username = MutableLiveData<String>()

    val user: LiveData<Resource<UserEntity>> = username.switchMap { username ->
        userRepository.getUserDetail(username).asLiveData(viewModelScope.coroutineContext)
    }

    fun getUserDetail(username: String) {
        this.username.value = username
    }

    fun setFavorite(user: UserEntity) {
        viewModelScope.launch {
            userRepository.setFavorite(user)
        }
    }

}
