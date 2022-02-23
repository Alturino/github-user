package com.onirutla.githubuser.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.onirutla.githubuser.data.UserItem
import com.onirutla.githubuser.util.JsonParser

class HomeViewModel : ViewModel() {
    private val _userItems = MutableLiveData<List<UserItem>>()
    val userItems: LiveData<List<UserItem>> get() = _userItems

    fun getUser(context: Context, fileName: String) {
        _userItems.value = JsonParser.readJson(context, fileName = fileName)?.users
    }
}
