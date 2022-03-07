package com.onirutla.githubuser.ui.detail

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
import com.google.android.material.tabs.TabLayoutMediator
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args.username?.let {
            viewModel.getUserDetail(it)
            sharedViewModel.setUsername(it)
        }

        viewModel.user.observe(viewLifecycleOwner, { resource ->
            when (resource) {
                is Resource.Empty -> {
                    binding.apply {
                        progressBar.hide()
                        progressBar.visibility = View.GONE
                    }
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    binding.apply {
                        progressBar.visibility = View.VISIBLE
                        progressBar.show()
                    }
                }
                is Resource.Success -> {
                    binding.apply {
                        val userEntity = resource.data
                        user = userEntity
                        progressBar.hide()
                        progressBar.visibility = View.GONE
                        var status = userEntity.isFavorite
                        setFabState(status)
                        fabFavorite.setOnClickListener {
                            viewModel.setFavorite(userEntity)
                            status = !status
                            setFabState(status)
                        }
                    }

                }
            }
        })

        val tabAdapter = TabAdapter(requireActivity())

        binding.apply {
            toolbar.setNavigationOnClickListener { it.findNavController().navigateUp() }
            viewPager.adapter = tabAdapter
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                val titles = listOf(
                    resources.getString(R.string.follower),
                    resources.getString(R.string.following)
                )
                tab.text = titles[position]
            }.attach()
        }
    }

    private fun setFabState(status: Boolean) {
        if (status) {
            binding.fabFavorite.setImageResource(R.drawable.ic_favorite_24)
        } else {
            binding.fabFavorite.setImageResource(R.drawable.ic_favorite_border_24)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
