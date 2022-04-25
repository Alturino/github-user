package com.onirutla.githubuser.data.source.remote.network

import com.onirutla.githubuser.data.source.remote.response.SearchResponse
import com.onirutla.githubuser.data.source.remote.response.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApiService {

    @GET(value = "search/users")
    suspend fun searchBy(@Query(value = "q") username: String): Response<SearchResponse>

    @GET(value = "users/{username}")
    suspend fun getDetailBy(@Path(value = "username") username: String): Response<UserResponse>

    @GET(value = "users/{username}/followers")
    suspend fun getFollowerBy(
        @Path(value = "username") username: String,
        @Query(value = "page") page: Int = 1
    ): Response<List<UserResponse>>

    @GET(value = "users/{username}/following")
    suspend fun getFollowingBy(@Path(value = "username") username: String): Response<List<UserResponse>>
}