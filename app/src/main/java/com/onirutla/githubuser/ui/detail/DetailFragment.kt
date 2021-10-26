package com.onirutla.githubuser.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.onirutla.githubuser.R
import com.onirutla.githubuser.databinding.FragmentDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val args: DetailFragmentArgs by navArgs()

    private val viewModel: DetailViewModel by viewModels()

    private val pagerAdapter: ViewPagerAdapter by lazy {
        ViewPagerAdapter(requireActivity())
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
        viewModel.getUser(args.username)
        binding.apply {
            viewModel = this@DetailFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
            setupTabLayout()
            toolbar.setNavigationOnClickListener {
                it.findNavController().navigateUp()
            }
        }
    }

    private fun setupTabLayout() {
        binding.viewPager.adapter = pagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            val tabs = listOf(
                resources.getString(R.string.header_follower),
                resources.getString(R.string.header_following)
            )
            tab.text = tabs[position]
        }.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}