package com.onirutla.githubuser.ui.detail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.onirutla.githubuser.ui.follower.FollowerFragment
import com.onirutla.githubuser.ui.following.FollowingFragment

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val fragments = listOf(FollowerFragment(), FollowingFragment())

    override fun getCount(): Int = fragments.size

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> fragments[0]
            1 -> fragments[1]
            else -> Fragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        val tabsTitle = listOf("Follower", "Following")
        return tabsTitle[position]
    }
}