package com.onirutla.githubuser.ui.detail.follower

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.onirutla.githubuser.data.Resource
import com.onirutla.githubuser.databinding.FragmentFollowerBinding
import com.onirutla.githubuser.ui.SharedViewModel
import com.onirutla.githubuser.ui.home.UserAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowerFragment : Fragment() {

    private var _binding: FragmentFollowerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FollowerViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val userAdapter by lazy { UserAdapter { _, _ -> } }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel.username.observe(viewLifecycleOwner, {
            viewModel.getUserFollowers(it)
        })

        viewModel.followers.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Empty -> {
                    binding.apply {
                        progressBar.hide()
                        progressBar.visibility = View.GONE
                    }
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    binding.apply {
                        progressBar.visibility = View.VISIBLE
                        progressBar.show()
                    }
                }
                is Resource.Success -> {
                    binding.apply {
                        progressBar.hide()
                        progressBar.visibility = View.GONE
                    }
                    userAdapter.submitList(it.data)
                }
            }
        })

        binding.userList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = userAdapter
            setHasFixedSize(true)
        }
    }

}
