package com.onirutla.githubuser.util

import androidx.recyclerview.widget.DiffUtil
import com.onirutla.githubuser.data.local.entity.UserEntity

class UserDiff(
    private val oldList: List<UserEntity>, private val newList: List<UserEntity>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList == newList

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]
}
