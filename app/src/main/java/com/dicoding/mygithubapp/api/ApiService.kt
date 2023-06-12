package com.dicoding.mygithubapp.api

import com.dicoding.mygithubapp.response.DetailUserResponse
import com.dicoding.mygithubapp.response.FollowResponseItem
import com.dicoding.mygithubapp.response.GithubResponse
import com.dicoding.mygithubapp.response.ItemsItem
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("users")
    fun getUsers(): Call<List<ItemsItem>>

    @GET("search/users")
    fun getUsername(
        @Query("q") query: String
    ): Call<GithubResponse>

    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String
    ): Call<List<FollowResponseItem>>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String
    ): Call<List<FollowResponseItem>>
}