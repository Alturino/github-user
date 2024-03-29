package com.onirutla.githubuser.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.navOptions
import com.google.android.material.transition.MaterialFadeThrough
import com.onirutla.githubuser.core.adapter.UserAdapter
import com.onirutla.githubuser.core.data.doWhen
import com.onirutla.githubuser.core.domain.data.User
import com.onirutla.githubuser.core.util.DeepLinkDestination
import com.onirutla.githubuser.core.util.hide
import com.onirutla.githubuser.core.util.show
import com.onirutla.githubuser.favorite.databinding.FragmentFavoriteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoriteViewModel by viewModels()

    private val favoriteAdapter by lazy {
        UserAdapter { view, user ->
            view.findNavController().navigate(
                deepLink = "${DeepLinkDestination.Detail.route}/${user.username}".toUri(),
                navOptions = navOptions { restoreState = true },
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        reenterTransition = MaterialFadeThrough().apply {
            duration = resources.getInteger(R.integer.material_motion_duration_long_1).toLong()
        }

        enterTransition = MaterialFadeThrough().apply {
            duration = resources.getInteger(R.integer.material_motion_duration_long_1).toLong()
        }

        exitTransition = MaterialFadeThrough().apply {
            duration = resources.getInteger(R.integer.material_motion_duration_long_1).toLong()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvFavorite.apply {
            setHasFixedSize(true)
            adapter = favoriteAdapter
        }

        viewModel.favorites.observe(viewLifecycleOwner) { uiState ->
            uiState.doWhen(
                error = { showError(message) },
                loading = { showLoading() },
                success = { showData(data) }
            )
        }

    }

    private fun showData(users: List<User>) {
        binding.apply {
            rvFavorite.show()
            progressBar.hide()
            favoritePlaceholder.hide()
        }
        favoriteAdapter.submitList(users)
    }

    private fun showError(message: String?) {
        binding.apply {
            progressBar.hide()
            favoritePlaceholder.show()
        }
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading() {
        binding.apply {
            rvFavorite.hide()
            progressBar.show()
            favoritePlaceholder.hide()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}