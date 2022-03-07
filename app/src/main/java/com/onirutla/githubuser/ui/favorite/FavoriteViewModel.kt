package com.onirutla.githubuser.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.onirutla.githubuser.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val favoriteUser = userRepository.getFavoriteUsers().asLiveData(viewModelScope.coroutineContext)

}
