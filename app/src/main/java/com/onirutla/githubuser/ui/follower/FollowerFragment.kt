package com.onirutla.githubuser.ui.follower

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.onirutla.githubuser.databinding.FragmentFollowerBinding
import com.onirutla.githubuser.ui.SharedViewModel
import com.onirutla.githubuser.ui.adapter.UserAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowerFragment : Fragment() {

    private var _binding: FragmentFollowerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FollowerViewModel by viewModels()
    private val activityViewModel: SharedViewModel by activityViewModels()

    private val followerAdapter by lazy {
        UserAdapter { view, user ->

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            activityViewModel.username.observe(viewLifecycleOwner, { username ->

                viewModel.getUser(username)

                viewModel.user.observe(viewLifecycleOwner, { followers ->
                    followerAdapter.submitList(followers)
                    binding.rvUser.apply {
                        adapter = followerAdapter
                        setHasFixedSize(true)
                    }
                })
            })
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}