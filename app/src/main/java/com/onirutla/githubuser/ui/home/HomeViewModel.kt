package com.onirutla.githubuser.ui.home

import androidx.lifecycle.*
import com.onirutla.githubuser.data.remote.FromNetwork
import com.onirutla.githubuser.data.Resource
import com.onirutla.githubuser.data.repository.Repository
import com.onirutla.githubuser.data.remote.response.UserResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _username = MutableLiveData<String>()

    val user: LiveData<Resource<List<UserResponse>>> = _username.switchMap { username ->
        liveData {
            repository.findUsersByUsername(username).collect { fromNetwork ->
                when (fromNetwork) {
                    is FromNetwork.Error -> emit(Resource.Error(message = fromNetwork.message))
                    is FromNetwork.Loading -> emit(Resource.Loading())
                    is FromNetwork.Success -> emit(Resource.Success(fromNetwork.data))
                }
            }
        }
    }

    fun findUser(username: String) {
        _username.value = username
    }

}
