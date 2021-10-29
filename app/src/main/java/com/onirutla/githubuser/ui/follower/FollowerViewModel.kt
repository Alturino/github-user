package com.onirutla.githubuser.ui.follower

import androidx.lifecycle.*
import com.onirutla.githubuser.data.remote.RemoteDataSource
import com.onirutla.githubuser.data.remote.response.UserResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowerViewModel @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : ViewModel() {

    private val _username = MutableLiveData<String>()

    val user: LiveData<List<UserResponse>> = _username.switchMap {
        remoteDataSource.getUserFollower(it)
    }

    fun getUser(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _username.postValue(username)
        }
    }

}