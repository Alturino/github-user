package com.onirutla.githubuser.ui.splashscreen

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import com.onirutla.githubuser.core.util.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    dataStoreManager: DataStoreManager
) : ViewModel() {

    val userPreferences: Flow<Boolean> = dataStoreManager.preferencesIsSeen

}
