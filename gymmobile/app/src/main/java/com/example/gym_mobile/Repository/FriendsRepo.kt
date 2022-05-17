package com.example.gym_mobile.Repository

import com.example.gym_mobile.Entities.Friend
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface FriendsRepo {
    @GET("friend/getRequestsByEmail/{email}")
    fun getFriendsByEmail(@Path("email") email: String?) : Call<List<Friend>>
}