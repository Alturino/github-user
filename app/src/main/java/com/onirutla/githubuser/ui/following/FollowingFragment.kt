package com.onirutla.githubuser.ui.following

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import com.onirutla.githubuser.data.repository.Resource
import com.onirutla.githubuser.databinding.FragmentFollowingBinding
import com.onirutla.githubuser.ui.SharedViewModel
import com.onirutla.githubuser.ui.adapter.UserAdapter
import com.onirutla.githubuser.ui.detail.DetailFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FollowingFragment : Fragment() {

    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FollowingViewModel by viewModels()
    private val activityViewModel: SharedViewModel by activityViewModels()

    private val followingAdapter by lazy {
        UserAdapter { view, user ->
            view.findNavController()
                .navigate(DetailFragmentDirections.actionDetailFragmentSelf(user.username))
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

        activityViewModel.username.observe(viewLifecycleOwner, { username ->

            viewModel.getUser(username)

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.user.collect {
                        when (it) {
                            is Resource.Success -> {
                                binding.progressBar.visibility = View.GONE
                                followingAdapter.submitList(it.data)
                            }
                            is Resource.Loading -> {
                                binding.progressBar.visibility = View.VISIBLE
                            }
                            is Resource.Error -> {
                                Toast.makeText(context, "", Toast.LENGTH_SHORT).show()
                            }
                        }

                        binding.rvUser.apply {
                            adapter = followingAdapter
                            setHasFixedSize(true)
                        }
                    }
                }
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
