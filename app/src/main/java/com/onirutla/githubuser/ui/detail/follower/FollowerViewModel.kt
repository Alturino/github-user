package com.onirutla.githubuser.ui.detail.follower

import androidx.lifecycle.*
import com.onirutla.githubuser.data.FromNetwork
import com.onirutla.githubuser.data.Resource
import com.onirutla.githubuser.data.repository.Repository
import com.onirutla.githubuser.data.response.UserResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class FollowerViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _username = MutableLiveData<String>()

    val followers: LiveData<Resource<List<UserResponse>>> = _username.switchMap { username ->
        liveData {
            repository.getUserFollowers(username).collect { fromNetwork ->
                when (fromNetwork) {
                    is FromNetwork.Error -> emit(Resource.Error(fromNetwork.message))
                    is FromNetwork.Loading -> emit(Resource.Loading())
                    is FromNetwork.Success -> emit(Resource.Success(fromNetwork.data))
                }
            }
        }
    }

    fun getUserFollowers(username: String) {
        _username.value = username
    }
}
