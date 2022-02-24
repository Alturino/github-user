package com.onirutla.githubuser.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.onirutla.githubuser.data.UserItem
import com.onirutla.githubuser.databinding.UserItemBinding
import com.onirutla.githubuser.util.GlideApp
import com.onirutla.githubuser.util.UserDiff

class UserAdapter(
    private val listener: (userItem: UserItem, view: View) -> Unit
) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    private val list = mutableListOf<UserItem>()

    fun submitList(list: List<UserItem>) {
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
        fun bind(userItem: UserItem) {
            binding.apply {
                user = userItem
                root.setOnClickListener { listener(userItem, it) }
                val context = userAvatar.context
                GlideApp.with(userAvatar).load(
                    context.resources.getIdentifier(
                        userItem.avatar,
                        "drawable",
                        context.packageName
                    )
                ).into(userAvatar)
            }
        }
    }
}
