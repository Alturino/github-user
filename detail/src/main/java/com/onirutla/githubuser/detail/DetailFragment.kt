package com.onirutla.githubuser.detail

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialContainerTransform
import com.onirutla.githubuser.core.data.doWhen
import com.onirutla.githubuser.core.domain.data.User
import com.onirutla.githubuser.core.util.hide
import com.onirutla.githubuser.core.util.show
import com.onirutla.githubuser.detail.databinding.FragmentDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val args: DetailFragmentArgs by navArgs()

    private val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            duration = resources.getInteger(R.integer.material_motion_duration_long_1).toLong()
            scrimColor = Color.TRANSPARENT
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = args.username
        viewModel.getUser(username)

        viewModel.user.observe(viewLifecycleOwner) { uiState ->
            uiState.doWhen(
                error = { showError(message) },
                loading = { showLoading() },
                success = { showData(data) }
            )
        }

        setupUI()
    }

    private fun showData(user: User) {
        hideLoading()
        binding.user = user
        var status = user.isFavorite
        setFabState(status)
        binding.fabFavorite.setOnClickListener {
            status = !status
            viewModel.setFavorite(user)
            setFabState(status)
            showToast(status)
        }
    }

    private fun showError(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT)
            .show()
    }

    private fun showToast(status: Boolean) {
        if (status)
            Toast.makeText(context, "Favorite", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(context, "UnFavorited", Toast.LENGTH_SHORT).show()
    }

    private fun setFabState(status: Boolean) {
        if (status) {
            binding.fabFavorite.setImageResource(R.drawable.ic_favorite_24)
        } else {
            binding.fabFavorite.setImageResource(R.drawable.ic_favorite_border_24)
        }
    }

    private fun showLoading() {
        binding.apply {
            appbar.hide()
            progressBar.show()
        }
    }

    private fun hideLoading() {
        binding.apply {
            appbar.show()
            progressBar.hide()
        }
    }

    private fun setupUI() {
        binding.apply {

            toolbar.setNavigationOnClickListener {
                it.findNavController().navigateUp()
            }

//            viewPager.adapter = pagerAdapter
//            tabLayout.setupWithViewPager(viewPager)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
