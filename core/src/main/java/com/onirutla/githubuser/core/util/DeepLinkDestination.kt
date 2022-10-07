package com.onirutla.githubuser.core.util

import com.onirutla.githubuser.core.util.Constant.BASE_DEEP_LINK

sealed class DeepLinkDestination(val route: String) {
    object Search : DeepLinkDestination("${BASE_DEEP_LINK}search")
    object Detail : DeepLinkDestination("${BASE_DEEP_LINK}detail")
    object Follower : DeepLinkDestination("${BASE_DEEP_LINK}follower")
    object Following : DeepLinkDestination("${BASE_DEEP_LINK}following")
    object Favorite : DeepLinkDestination("${BASE_DEEP_LINK}favorite")
    object Setting : DeepLinkDestination("${BASE_DEEP_LINK}setting")
}