package com.onirutla.githubuser.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.onirutla.githubuser.data.source.local.entity.UserEntity
import com.onirutla.githubuser.databinding.UserItemBinding
import com.onirutla.githubuser.util.Comparator

class UserAdapter(
    private val listener: (view: View, user: UserEntity) -> Unit
) : ListAdapter<UserEntity, UserAdapter.ViewHolder>(Comparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = UserItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            binding.user = getItem(position)
            itemView.setOnClickListener {
                listener(it, getItem(position))
            }
        }
    }

    inner class ViewHolder(val binding: UserItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}