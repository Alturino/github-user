package com.onirutla.githubuser.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.onirutla.githubuser.data.source.local.entity.UserEntity
import com.onirutla.githubuser.databinding.UserItemBinding

class UserAdapter(private val listener: (view: View, user: UserEntity) -> Unit) :
    ListAdapter<UserEntity, UserAdapter.ViewHolder>(Diff) {

    object Diff : DiffUtil.ItemCallback<UserEntity>() {
        override fun areItemsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean =
            oldItem == newItem
    }

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