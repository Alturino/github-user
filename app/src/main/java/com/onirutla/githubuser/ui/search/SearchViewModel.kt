package com.onirutla.githubuser.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.onirutla.githubuser.data.remote.RemoteDataSource
import com.onirutla.githubuser.data.remote.response.SearchResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
) : ViewModel() {

    private val _username = MutableLiveData<String>()

    val users: LiveData<SearchResponse> = _username.switchMap {
        remoteDataSource.getUserSearch(it)
    }

    fun search(username: String) {
        _username.value = username
    }

}