package com.onirutla.githubuser.ui.detail

import androidx.lifecycle.*
import com.onirutla.githubuser.data.remote.RemoteDataSource
import com.onirutla.githubuser.data.remote.response.UserResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : ViewModel() {

    private val _username = MutableLiveData<String>()

    val user: LiveData<UserResponse> = _username.switchMap {
        remoteDataSource.getUserDetail(it)
    }

    fun getUser(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _username.postValue(username)
        }
    }
}