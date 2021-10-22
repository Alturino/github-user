package com.onirutla.githubuser.ui.search

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.onirutla.githubuser.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels()

    private val searchAdapter by lazy {
        SearchAdapter { view, user ->

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
        with(binding) {
            toolbar.setNavigationOnClickListener {
                it.findNavController().navigateUp()
            }
            searchBar.setOnEditorActionListener { v, actionId, _ ->
                return@setOnEditorActionListener isSearchClicked(v, actionId)
            }
            rvUser.apply {
                adapter = searchAdapter
                setHasFixedSize(true)
            }
        }
    }

    private fun isSearchClicked(view: View, actionId: Int): Boolean {
        return if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            searchUser()
            view.closeSoftKeyboard(requireContext())
            true
        } else false
    }

    private fun searchUser() {
        with(binding) {
            if (searchBar.text!!.isNotBlank()) {
                rvUser.requestFocus()
                getUserSearch(searchBar.text.toString())
            }
        }
    }

    private fun getUserSearch(keyword: String) {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getUserSearch(username = keyword).observe(viewLifecycleOwner, {
                searchAdapter.submitList(it.items)
            })
        }
    }

    private fun View.closeSoftKeyboard(context: Context) {
        val inputMethodManager =
            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(this.windowToken, 0)
        this.clearFocus()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}