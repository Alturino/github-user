package com.onirutla.githubuser.following

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
import com.onirutla.githubuser.following.databinding.FragmentFollowingBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FollowingFragment : Fragment() {

    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FollowingViewModel by viewModels()
    private val args: FollowingFragmentArgs by navArgs()

    private val followingAdapter by lazy {
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
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = args.username
        viewModel.getFollowings(username)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                viewModel.user.collect {
                    followingAdapter.submitData(it)
                }
            }
        }

        binding.rvUser.apply {
            adapter = followingAdapter
            setHasFixedSize(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
