package com.onirutla.githubuser.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.onirutla.githubuser.data.response.UserResponse
import com.onirutla.githubuser.databinding.UserItemBinding
import com.onirutla.githubuser.util.UserDiff

class UserAdapter(
    private val listener: (user: UserResponse, view: View) -> Unit
) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    private val list = mutableListOf<UserResponse>()

    fun submitList(list: List<UserResponse>) {
        val diffUser = DiffUtil.calculateDiff(UserDiff(this.list, list))
        this.list.clear()
        this.list.addAll(list)
        diffUser.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class ViewHolder(
        private val binding: UserItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener { listener(list[adapterPosition], it) }
        }

        fun bind(user: UserResponse) {
            binding.user = user
        }
    }
}
