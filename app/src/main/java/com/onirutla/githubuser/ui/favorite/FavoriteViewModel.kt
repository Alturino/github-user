package com.onirutla.githubuser.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.onirutla.githubuser.data.Resource
import com.onirutla.githubuser.data.UserDTO
import com.onirutla.githubuser.data.source.UserDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val userDataSource: UserDataSource
) : ViewModel() {

    val favorites: LiveData<Resource<List<UserDTO>>> =
        userDataSource.getUsersFavorite().asLiveData(viewModelScope.coroutineContext)


}