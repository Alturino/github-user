package com.onirutla.githubuser.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.onirutla.githubuser.data.UIState
import com.onirutla.githubuser.data.repository.UserRepository
import com.onirutla.githubuser.data.source.local.entity.UserEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    userRepository: UserRepository
) : ViewModel() {

    val favorites: LiveData<UIState<List<UserEntity>>> = userRepository.getFavorite()
        .asLiveData(viewModelScope.coroutineContext)
}