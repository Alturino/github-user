package com.onirutla.githubuser

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.onirutla.githubuser.databinding.ActivitySplashBinding
import com.onirutla.githubuser.util.Constant
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launchWhenStarted {
            delay(Constant.DELAY_TO_MAIN_ACTIVITY)
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }
    }

}
