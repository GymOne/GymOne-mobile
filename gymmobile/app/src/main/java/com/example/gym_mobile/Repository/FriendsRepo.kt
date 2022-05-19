package com.example.gym_mobile.Repository

import com.example.gym_mobile.Dto.CreateWorkoutSessionDto
import com.example.gym_mobile.Dto.SubmitFriendRequestDto
import com.example.gym_mobile.Entities.Friend
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FriendsRepo {
    @GET("friend/getRequestsByEmail/{email}")
    fun getFriendsByEmail(@Path("email") email: String?) : Call<List<Friend>>

    @POST("/friend/submitRequest")
    suspend fun submitFriendRequest(@Body submitFriendRequestDto: SubmitFriendRequestDto?) : Response<Any>
}