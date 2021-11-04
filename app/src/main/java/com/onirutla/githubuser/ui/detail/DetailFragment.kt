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

        viewModel.user.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    binding.apply {
                        appbar.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                    }
                    binding.user = it.data
                }
                is Resource.Loading -> {
                    binding.apply {
                        appbar.visibility = View.GONE
                        progressBar.visibility = View.VISIBLE
                    }
                }
                is Resource.Error -> Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
        })

        setupUI()
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}