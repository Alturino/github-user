package com.onirutla.githubuser.follower

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navOptions
import com.onirutla.githubuser.core.adapter.UserPagingAdapter
import com.onirutla.githubuser.core.util.DeepLinkDestination
import com.onirutla.githubuser.follower.databinding.FragmentFollowerBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FollowerFragment : Fragment() {

    private var _binding: FragmentFollowerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FollowerViewModel by viewModels()

    private val args: FollowerFragmentArgs by navArgs()

    private val followerAdapter by lazy {
        UserPagingAdapter { view, user ->
            view.findNavController().navigate(
                deepLink = "${DeepLinkDestination.Detail.route}/${user.username}".toUri(),
                navOptions = navOptions { restoreState = true },
            )
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

        val username = args.username
        viewModel.getFollowers(username)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.user.collect {
                    followerAdapter.submitData(it)
                }
            }
        }

        binding.rvUser.apply {
            adapter = followerAdapter
            setHasFixedSize(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
