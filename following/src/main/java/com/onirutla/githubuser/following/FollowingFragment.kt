package com.onirutla.githubuser.following

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.onirutla.githubuser.following.databinding.FragmentFollowingBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FollowingFragment : Fragment() {

    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FollowingViewModel by viewModels()
//    private val activityViewModel: SharedViewModel by activityViewModels()

//    private val followingAdapter by lazy {
//        UserPagingAdapter { view, user ->
//            view.findNavController()
//                .navigate(DetailFragmentDirections.actionDetailFragmentSelf(user.username))
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        activityViewModel.username.observe(viewLifecycleOwner) { username ->
//            viewModel.getUser(username)
//        }

//        viewLifecycleOwner.lifecycleScope.launch {
//            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.user.collect {
//                    followingAdapter.submitData(it)
//                }
//            }
//        }
//
//        binding.rvUser.apply {
//            adapter = followingAdapter
//            setHasFixedSize(true)
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
