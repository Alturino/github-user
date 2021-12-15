package com.onirutla.githubuser.ui.detail

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialContainerTransform
import com.onirutla.githubuser.R
import com.onirutla.githubuser.data.Resource
import com.onirutla.githubuser.databinding.FragmentDetailBinding
import com.onirutla.githubuser.ui.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val args: DetailFragmentArgs by navArgs()

    private val viewModel: DetailViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val pagerAdapter: ViewPagerAdapter by lazy {
        ViewPagerAdapter(childFragmentManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
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
        sharedViewModel.setUsername(username)

        viewModel.user.observe(viewLifecycleOwner, { resource ->
            when (resource) {
                is Resource.Success -> {
                    hideLoading()
                    binding.user = resource.data
                    var status = resource.data.isFavorite
                    setFabState(status)
                    binding.fabFavorite.setOnClickListener {
                        status = !status
                        viewModel.setFavorite(resource.data)
                        setFabState(status)
                        showToast(status)
                    }
                }
                is Resource.Loading -> showLoading()
                is Resource.Error -> showError(resource.message)
            }
        })

        setupUI()
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
            appbar.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }
    }

    private fun hideLoading() {
        binding.apply {
            appbar.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }

    private fun setupUI() {
        binding.apply {

            toolbar.setNavigationOnClickListener {
                it.findNavController().navigateUp()
            }

            viewPager.adapter = pagerAdapter
            tabLayout.setupWithViewPager(viewPager)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}