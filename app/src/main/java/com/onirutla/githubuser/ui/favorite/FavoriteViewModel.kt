package com.onirutla.githubuser.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onirutla.githubuser.data.Resource
import com.onirutla.githubuser.data.UserDTO
import com.onirutla.githubuser.data.source.UserDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    userDataSource: UserDataSource
) : ViewModel() {

    val favorites: StateFlow<Resource<List<UserDTO>>> =
        userDataSource.getUsersFavorite().stateIn(
            initialValue = Resource.Loading(),
            scope = viewModelScope,
            started = WhileSubscribed(5000)
        )
}