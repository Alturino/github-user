package com.onirutla.githubuser.ui.following

import androidx.lifecycle.*
import com.onirutla.githubuser.data.Resource
import com.onirutla.githubuser.data.source.UserDataSource
import com.onirutla.githubuser.data.source.local.entity.UserEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowingViewModel @Inject constructor(
    private val userDataSource: UserDataSource
) : ViewModel() {

    private val _username = MutableLiveData<String>()

    val user: LiveData<Resource<List<UserEntity>>> = _username.switchMap {
        userDataSource.getUsersFollowing(it).asLiveData()
    }

    fun getUser(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _username.postValue(username)
        }
    }

}