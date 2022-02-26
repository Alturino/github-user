package com.onirutla.githubuser.data.api

import com.onirutla.githubuser.data.response.SearchResponse
import com.onirutla.githubuser.data.response.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET(value = "search/users")
    suspend fun findUsersByUsername(@Query(value = "q") username: String): Response<SearchResponse>

    @GET(value = "users/{username}")
    suspend fun getUserDetail(@Path(value = "username") username: String): Response<UserResponse>

    @GET(value = "users/{username}/followers")
    suspend fun getUserFollowers(@Path(value = "username") username: String): Response<List<UserResponse>>

    @GET(value = "users/{username}/following")
    suspend fun getUserFollowings(@Path(value = "username") username: String): Response<List<UserResponse>>

}
