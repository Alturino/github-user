package com.onirutla.githubuser.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.onirutla.githubuser.data.Resource
import com.onirutla.githubuser.databinding.FragmentFavoriteBinding
import com.onirutla.githubuser.ui.adapter.UserAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoriteViewModel by viewModels()

    private val favoriteAdapter by lazy {
        UserAdapter { view, user ->
            view.findNavController()
                .navigate(FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(user.username))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.favorites.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    binding.apply {
                        rvFavorite.visibility = View.GONE
                        progressBar.visibility = View.VISIBLE
                    }
                }
                is Resource.Success -> {
                    binding.apply {
                        rvFavorite.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                    }
                    favoriteAdapter.submitList(it.data)
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}