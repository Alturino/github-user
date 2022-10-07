package com.onirutla.githubuser.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.onirutla.githubuser.core.data.UIState
import com.onirutla.githubuser.core.domain.data.User
import com.onirutla.githubuser.core.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    userRepository: UserRepository
) : ViewModel() {

    val favorites: LiveData<UIState<List<User>>> = userRepository.getFavorite()
        .asLiveData(viewModelScope.coroutineContext)
}