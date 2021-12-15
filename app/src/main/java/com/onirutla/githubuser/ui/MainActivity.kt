package com.onirutla.githubuser.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.transition.MaterialSharedAxis
import com.onirutla.githubuser.MainNavDirections
import com.onirutla.githubuser.R
import com.onirutla.githubuser.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    private val currentFragment: Fragment?
        get() = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
            ?.childFragmentManager
            ?.fragments
            ?.first()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.findNavController()

        setupBottomNav()
    }

    private fun setupBottomNav() {

        binding.apply {
            bottomNav.setupWithNavController(navController = navController)
            fab.setOnClickListener {
                navigateToSearch()
            }
        }

        setupDestinationListener(navController)
    }

    private fun navigateToSearch() {
        currentFragment?.apply {
            exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
                duration =
                    resources.getInteger(R.integer.material_motion_duration_long_1).toLong()
            }

            reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
                duration =
                    resources.getInteger(R.integer.material_motion_duration_long_1).toLong()
            }
        }
        navController.navigate(MainNavDirections.actionGlobalSearchFragment())
    }

    private fun setupDestinationListener(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.searchFragment -> {
                    hideBottomAppbar()
                }
                R.id.detailFragment -> {
                    hideBottomAppbar()
                }
                else -> {
                    showBottomAppbar()
                }
            }
        }
    }

    private fun showBottomAppbar() {
        binding.run {
            bottomAppbar.visibility = View.VISIBLE
            bottomAppbar.performShow()
            fab.show()
        }
    }

    private fun hideBottomAppbar() {
        binding.run {
            bottomAppbar.visibility = View.GONE
            bottomAppbar.performHide()
            fab.hide()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }
}