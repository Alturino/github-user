package com.onirutla.githubuser.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.onirutla.githubuser.databinding.ActivityIntroScreenBinding
import com.onirutla.githubuser.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroScreenActivity : AppCompatActivity() {

    private val introAdapter by lazy { IntroAdapter(introModels) }

    private val viewModel: IntroScreenViewModel by viewModels()

    private lateinit var binding: ActivityIntroScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding.introViewpager) {

            adapter = introAdapter

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    Log.d("intro screen activity", "$position")
                    if (position == introAdapter.itemCount - 1) {
                        binding.buttonSkip.visibility = View.GONE
                        with(binding.buttonDone) {
                            visibility = View.VISIBLE
                            setOnClickListener {
                                viewModel.setUserPreferences()
                                startActivity(
                                    Intent(
                                        this@IntroScreenActivity,
                                        MainActivity::class.java
                                    )
                                )
                                finish()
                            }
                        }
                    } else {
                        binding.buttonSkip.visibility = View.VISIBLE
                        binding.buttonSkip.setOnClickListener {
                            binding.introViewpager.currentItem = introAdapter.itemCount - 1
                        }
                    }
                }
            })
        }

        TabLayoutMediator(binding.tabLayout, binding.introViewpager) { tab, position ->

        }.attach()
    }
}
