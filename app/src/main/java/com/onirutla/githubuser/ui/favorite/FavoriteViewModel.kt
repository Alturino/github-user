package com.onirutla.githubuser.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onirutla.githubuser.data.Resource
import com.onirutla.githubuser.data.UserDTO
import com.onirutla.githubuser.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    userRepository: UserRepository
) : ViewModel() {

    val favorites: StateFlow<Resource<List<UserDTO>>> =
        userRepository.getUsersFavorite().stateIn(
            initialValue = Resource.Loading(),
            scope = viewModelScope,
            started = WhileSubscribed(5000)
        )
}
