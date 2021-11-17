package com.onirutla.githubuser.util

import androidx.recyclerview.widget.DiffUtil
import com.onirutla.githubuser.data.source.local.entity.UserEntity

object Constant {
    const val BASE_URL = "https://api.github.com/"

    val diff = object : DiffUtil.ItemCallback<UserEntity>() {
        override fun areItemsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean =
            oldItem == newItem
    }
    const val DB_NAME = "github_user_db"
}