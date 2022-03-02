package com.onirutla.githubuser.ui.detail

import androidx.lifecycle.*
import com.onirutla.githubuser.data.remote.FromNetwork
import com.onirutla.githubuser.data.Resource
import com.onirutla.githubuser.data.repository.Repository
import com.onirutla.githubuser.data.remote.response.UserResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    val username = MutableLiveData<String>()

    val user: LiveData<Resource<UserResponse>> = username.switchMap { username ->
        liveData {
            repository.getUserDetail(username).collect { fromNetwork ->
                when (fromNetwork) {
                    is FromNetwork.Error -> emit(Resource.Error(fromNetwork.message))
                    is FromNetwork.Loading -> emit(Resource.Loading())
                    is FromNetwork.Success -> emit(Resource.Success(fromNetwork.data))
                }
            }
        }
    }

    fun getUserDetail(username: String) {
        this.username.value = username
    }

}
