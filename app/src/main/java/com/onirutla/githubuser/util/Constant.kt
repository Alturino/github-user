package com.onirutla.githubuser.util

import androidx.recyclerview.widget.DiffUtil
import com.onirutla.githubuser.data.repository.UserDTO

object Constant {
    const val BASE_URL = "https://api.github.com/"

    val diff = object : DiffUtil.ItemCallback<UserDTO>() {
        override fun areItemsTheSame(oldItem: UserDTO, newItem: UserDTO): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: UserDTO, newItem: UserDTO): Boolean =
            oldItem == newItem
    }
    const val DB_NAME = "github_user_db"

    const val INTRO_DATASTORE = "intro_datastore"
    const val KEY_INTRO_PREFERENCES = "key_intro_preferences"
}
