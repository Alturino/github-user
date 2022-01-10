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
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialSharedAxis
import com.onirutla.githubuser.R
import com.onirutla.githubuser.data.repository.Resource
import com.onirutla.githubuser.databinding.FragmentSearchBinding
import com.onirutla.githubuser.ui.adapter.UserAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels()

    private val searchAdapter by lazy {
        UserAdapter { view, user ->
            val userDetailTransitionName = getString(R.string.list_transition_detail)
            val extras = FragmentNavigatorExtras(view to userDetailTransitionName)

            exitTransition = MaterialElevationScale(false).apply {
                duration = resources.getInteger(R.integer.material_motion_duration_long_1).toLong()
            }

            reenterTransition = MaterialElevationScale(true).apply {
                duration = resources.getInteger(R.integer.material_motion_duration_long_1).toLong()
            }

            view.findNavController()
                .navigate(
                    SearchFragmentDirections.actionSearchFragmentToDetailFragment(user.username),
                    extras
                )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            duration = resources.getInteger(R.integer.material_motion_duration_long_1).toLong()
        }

        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = resources.getInteger(R.integer.material_motion_duration_long_1).toLong()
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

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.users.collectLatest {
                    when (it) {
                        is Resource.Success -> {
                            binding.apply {
                                progressBar.visibility = View.GONE
                                binding.rvUser.visibility = View.VISIBLE
                            }
                            searchAdapter.submitList(it.data)
                        }
                        is Resource.Loading -> {
                            binding.apply {
                                progressBar.visibility = View.VISIBLE
                                rvUser.visibility = View.GONE
                            }
                        }
                        is Resource.Error -> Toast.makeText(context, it.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }

        setupUI()
    }

    private fun setupUI() {
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
        viewModel.search(keyword)
    }

    private fun View.closeSoftKeyboard(context: Context) {
        val inputMethodManager =
            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(this.windowToken, 0)
        this.clearFocus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
