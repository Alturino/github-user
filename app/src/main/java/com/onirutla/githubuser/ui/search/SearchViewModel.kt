package com.onirutla.githubuser.ui.search

import androidx.lifecycle.ViewModel
import com.onirutla.githubuser.data.remote.RemoteDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
) : ViewModel() {

    fun getUserSearch(username: String) = remoteDataSource.getUserSearch(username)

}