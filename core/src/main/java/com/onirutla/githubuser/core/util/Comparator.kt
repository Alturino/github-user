package com.onirutla.githubuser.core.util

import androidx.recyclerview.widget.DiffUtil
import com.onirutla.githubuser.core.domain.data.User

object Comparator : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
        oldItem == newItem
}
