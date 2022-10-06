package com.onirutla.githubuser.follower

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import com.onirutla.githubuser.core.adapter.UserPagingAdapter
import com.onirutla.githubuser.follower.databinding.FragmentFollowerBinding
import com.onirutla.githubuser.ui.SharedViewModel
import com.onirutla.githubuser.ui.detail.DetailFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FollowerFragment : Fragment() {

    private var _binding: FragmentFollowerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FollowerViewModel by viewModels()
    private val activityViewModel: SharedViewModel by activityViewModels()

    private val followerAdapter by lazy {
        UserPagingAdapter { view, user ->
            view.findNavController()
                .navigate(DetailFragmentDirections.actionDetailFragmentSelf(user.username))
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

        activityViewModel.username.observe(viewLifecycleOwner) { username ->
            viewModel.getUser(username)
        }

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
