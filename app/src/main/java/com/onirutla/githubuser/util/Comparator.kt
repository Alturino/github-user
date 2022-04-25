package com.onirutla.githubuser.util

import androidx.recyclerview.widget.DiffUtil
import com.onirutla.githubuser.data.source.local.entity.UserEntity

object Comparator : DiffUtil.ItemCallback<UserEntity>() {
    override fun areItemsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean =
        oldItem == newItem
}
