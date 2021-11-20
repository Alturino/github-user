package com.onirutla.githubuser.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onirutla.githubuser.data.Resource
import com.onirutla.githubuser.data.source.UserDataSource
import com.onirutla.githubuser.data.source.local.entity.UserEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val userDataSource: UserDataSource
) : ViewModel() {

    val favorites: StateFlow<Resource<List<UserEntity>>> =
        userDataSource.getUsersFavorite().stateIn(
            initialValue = Resource.Loading(),
            scope = viewModelScope,
            started = WhileSubscribed(5000)
        )
}