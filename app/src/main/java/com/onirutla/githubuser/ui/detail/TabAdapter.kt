package com.onirutla.githubuser.ui.detail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.onirutla.githubuser.ui.detail.follower.FollowerFragment
import com.onirutla.githubuser.ui.detail.following.FollowingFragment

class TabAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    private val fragments by lazy { listOf(FollowerFragment(), FollowingFragment()) }

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> fragments[0]
        1 -> fragments[1]
        else -> Fragment()
    }

}
