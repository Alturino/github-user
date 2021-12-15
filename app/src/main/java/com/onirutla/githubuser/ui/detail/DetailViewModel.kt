package com.onirutla.githubuser.ui.detail

import androidx.lifecycle.*
import com.onirutla.githubuser.data.Resource
import com.onirutla.githubuser.data.UserDTO
import com.onirutla.githubuser.data.source.UserDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val userDataSource: UserDataSource
) : ViewModel() {

    private val _username = MutableLiveData<String>()

    val user: LiveData<Resource<UserDTO>> = _username.switchMap {
        userDataSource.getUserDetail(it).asLiveData()
    }

    fun setFavorite(userEntity: UserDTO) {
        viewModelScope.launch {
            userDataSource.setUserFavorite(userEntity)
        }
    }

    fun getUser(username: String) {
        viewModelScope.launch {
            _username.postValue(username)
        }
    }
}