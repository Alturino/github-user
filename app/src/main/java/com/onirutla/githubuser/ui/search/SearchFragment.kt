package com.onirutla.githubuser.ui.search

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.onirutla.githubuser.data.Resource
import com.onirutla.githubuser.databinding.FragmentSearchBinding
import com.onirutla.githubuser.ui.adapter.UserAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels()

    private val userAdapter by lazy {
        UserAdapter { user, view ->
            view.findNavController()
                .navigate(
                    SearchFragmentDirections.actionSearchFragmentToDetailFragment(user.username)
                )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
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
        with(binding) {
            toolbar.setNavigationOnClickListener {
                it.findNavController().navigateUp()
            }
            searchBar.setOnEditorActionListener { v, actionId, _ ->
                return@setOnEditorActionListener isSearchClicked(v, actionId)
            }
            userList.apply {
                adapter = userAdapter
                setHasFixedSize(true)
            }
        }
    }

    private fun searchUser() {
        binding.apply {
            if (searchBar.text!!.isNotBlank()) {
                userList.requestFocus()
                viewModel.findUser(searchBar.text.toString())
            }
        }
    }

    private fun isSearchClicked(view: View, actionId: Int): Boolean =
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            searchUser()
            view.closeSoftKeyboard(requireContext())
            true
        } else false


    private fun View.closeSoftKeyboard(context: Context) {
        val inputMethodManager =
            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(this.windowToken, 0)
        this.clearFocus()
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
