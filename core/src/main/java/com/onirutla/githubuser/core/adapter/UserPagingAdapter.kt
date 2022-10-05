package com.onirutla.githubuser.core.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.onirutla.githubuser.core.databinding.UserItemBinding
import com.onirutla.githubuser.core.domain.data.User
import com.onirutla.githubuser.core.util.Comparator

class UserPagingAdapter(
    private val listener: (view: View, user: User) -> Unit
) : PagingDataAdapter<User, UserPagingAdapter.ViewHolder>(Comparator) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            binding.user = getItem(position)
            itemView.setOnClickListener { view ->
                getItem(position)?.let { user -> listener(view, user) }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = UserItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    inner class ViewHolder(
        val binding: UserItemBinding
    ) : RecyclerView.ViewHolder(binding.root)

}