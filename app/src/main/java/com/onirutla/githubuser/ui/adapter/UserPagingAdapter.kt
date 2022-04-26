package com.onirutla.githubuser.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.onirutla.githubuser.data.source.local.entity.UserEntity
import com.onirutla.githubuser.databinding.UserItemBinding
import com.onirutla.githubuser.util.Comparator

class UserPagingAdapter(
    private val listener: (view: View, user: UserEntity) -> Unit
) : PagingDataAdapter<UserEntity, UserPagingAdapter.ViewHolder>(Comparator) {

    override fun onBindViewHolder(holder: UserPagingAdapter.ViewHolder, position: Int) {
        holder.apply {
            binding.user = getItem(position)
            itemView.setOnClickListener { view ->
                getItem(position)?.let { userEntity -> listener(view, userEntity) }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserPagingAdapter.ViewHolder {
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