package com.onirutla.githubuser.ui.onboarding

import androidx.annotation.DrawableRes
import com.onirutla.githubuser.R

data class IntroModel(
    val title: String,
    val description: String,
    @DrawableRes val image: Int
)

val introModels = listOf(
    IntroModel(
        title = "Search User",
        description = "Find thousand or million of user in GitHub and see how much their follower, who are they following and their public repos.",
        image = R.drawable.search_illustration
    ),
    IntroModel(
        title = "Detail User",
        description = "See their detail and see their follower and who are they following and their public repos",
        image = R.drawable.detail_illustration
    ),
    IntroModel(
        title = "Favorite User",
        description = "Save user that you like so you can see them again in someday",
        image = R.drawable.favorite_illustration
    )
)
