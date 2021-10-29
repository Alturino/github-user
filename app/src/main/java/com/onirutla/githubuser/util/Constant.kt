package com.onirutla.githubuser.util

import androidx.recyclerview.widget.DiffUtil
import com.onirutla.githubuser.BuildConfig
import com.onirutla.githubuser.data.remote.response.UserResponse

object Constant {
    const val BASE_URL = "https://api.github.com/"

    val diff = object : DiffUtil.ItemCallback<UserResponse>() {
        override fun areItemsTheSame(oldItem: UserResponse, newItem: UserResponse): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: UserResponse, newItem: UserResponse): Boolean =
            oldItem == newItem
    }

    const val API_KEY = BuildConfig.API_KEY
}