package com.example.myfirstgithubuser.data.retrofit
import com.example.myfirstgithubuser.data.response.DetailUserResponse
import com.example.myfirstgithubuser.data.response.GithubResponse
import com.example.myfirstgithubuser.data.response.ItemsItem
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users?")
    fun getUser(
        @Query("q") q: String,
    ): Call<GithubResponse>

    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String,
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String
    ): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String
    ): Call<List<ItemsItem>>
}