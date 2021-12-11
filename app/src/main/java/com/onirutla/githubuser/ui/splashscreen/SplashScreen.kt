package com.onirutla.githubuser.ui.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.onirutla.githubuser.R
import com.onirutla.githubuser.ui.MainActivity
import com.onirutla.githubuser.ui.onboarding.IntroScreenActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SplashScreen : AppCompatActivity() {

    private val viewModel: SplashScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        lifecycleScope.launchWhenStarted {
            viewModel.userPreferences.collect {
                if (it) {
                    delay(2000)
                    startActivity(
                        Intent(
                            this@SplashScreen,
                            MainActivity::class.java
                        )
                    )
                    finish()
                } else {
                    delay(2000)
                    startActivity(Intent(this@SplashScreen, IntroScreenActivity::class.java))
                    finish()
                }
            }

        }
    }
}
