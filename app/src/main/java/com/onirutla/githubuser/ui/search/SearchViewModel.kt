package com.onirutla.githubuser.ui.search

import androidx.lifecycle.*
import com.onirutla.githubuser.data.Resource
import com.onirutla.githubuser.data.UserDTO
import com.onirutla.githubuser.data.source.UserDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val userDataSource: UserDataSource
) : ViewModel() {

    private val _username = MutableLiveData<String>()

    val users: LiveData<Resource<List<UserDTO>>> = _username.switchMap {
        userDataSource.getUsersSearch(it).asLiveData()
    }

    fun search(username: String) {
        viewModelScope.launch {
            _username.value = username
        }
    }

}