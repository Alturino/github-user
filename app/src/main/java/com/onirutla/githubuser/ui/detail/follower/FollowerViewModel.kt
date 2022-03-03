package com.onirutla.githubuser.ui.detail.follower

import androidx.lifecycle.*
import com.onirutla.githubuser.data.Resource
import com.onirutla.githubuser.data.local.entity.UserEntity
import com.onirutla.githubuser.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FollowerViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _username = MutableLiveData<String>()

    val followers: LiveData<Resource<List<UserEntity>>> = _username.switchMap { username ->
        userRepository.getUserFollowers(username).asLiveData(viewModelScope.coroutineContext)
    }

    fun getUserFollowers(username: String) {
        _username.value = username
    }
}
