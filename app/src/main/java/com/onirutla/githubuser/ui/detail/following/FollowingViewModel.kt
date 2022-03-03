package com.onirutla.githubuser.ui.detail.following

import androidx.lifecycle.*
import com.onirutla.githubuser.data.Resource
import com.onirutla.githubuser.data.local.entity.UserEntity
import com.onirutla.githubuser.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FollowingViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _username = MutableLiveData<String>()

    val followings: LiveData<Resource<List<UserEntity>>> = _username.switchMap { username ->
        userRepository.getUserFollowings(username).asLiveData(viewModelScope.coroutineContext)
    }

    fun getUserFollowings(username: String) {
        _username.value = username
    }

}
