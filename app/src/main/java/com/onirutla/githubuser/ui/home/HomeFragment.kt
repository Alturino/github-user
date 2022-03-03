package com.onirutla.githubuser.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.onirutla.githubuser.R
import com.onirutla.githubuser.data.Resource
import com.onirutla.githubuser.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private val userAdapter by lazy {
        UserAdapter { user, view ->
            view.findNavController()
                .navigate(
                    HomeFragmentDirections.actionHomeFragmentToDetailFragment(user.username)
                )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()

        observeViewModel()

        setupSearchView()
    }

    private fun observeViewModel() {
        viewModel.user.observe(viewLifecycleOwner, {
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
    }

    private fun setupSearchView() {
        val searchView = binding.toolbar.menu.findItem(R.id.search).actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.findUser(it) }
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun setupAdapter() {
        binding.userList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = userAdapter
            setHasFixedSize(true)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
