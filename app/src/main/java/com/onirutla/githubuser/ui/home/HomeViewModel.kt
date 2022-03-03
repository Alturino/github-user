package com.onirutla.githubuser.ui.home

import androidx.lifecycle.*
import com.onirutla.githubuser.data.Resource
import com.onirutla.githubuser.data.local.entity.UserEntity
import com.onirutla.githubuser.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _username = MutableLiveData<String>()

    val user: LiveData<Resource<List<UserEntity>>> = _username.switchMap { username ->
        userRepository.findUserByUsername(username).asLiveData()
    }

    fun findUser(username: String) {
        viewModelScope.launch {
            _username.postValue(username)
        }
    }

}
