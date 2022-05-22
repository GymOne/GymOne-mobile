package com.example.gym_mobile.Repository

import com.example.gym_mobile.Dto.CreateWorkoutSessionDto
import com.example.gym_mobile.Dto.GetFriendsDto
import com.example.gym_mobile.Dto.SubmitFriendRequestDto
import com.example.gym_mobile.Entities.Friend
import com.example.gym_mobile.Entities.FriendRequest
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FriendsRepo {
    @GET("friend/getFriendsByEmail/{email}")
    fun getFriendsByEmail(@Path("email") email: String?) : Call<List<GetFriendsDto>>

    @GET("friend/getNamesForRequests/{email}")
    fun getFriendsReqWNames(@Path("email") email: String?) : Call<List<FriendRequest>>

    @POST("/friend/submitRequest")
    suspend fun submitFriendRequest(@Body submitFriendRequestDto: SubmitFriendRequestDto?) : Response<Any>

    @POST("friend/removeRequest")
    suspend fun removeRequest(@Body submitFriendRequestDto: SubmitFriendRequestDto?) : Response<Any>
//Maybe ??????
    @POST("/friend/actionOnRequet")
    suspend fun actionUponRequest(@Body actUponRequest: SubmitFriendRequestDto?) : Response<Any>
}
