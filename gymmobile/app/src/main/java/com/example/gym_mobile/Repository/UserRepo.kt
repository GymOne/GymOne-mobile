package com.example.gym_mobile.Repository

import com.example.gym_mobile.Dto.GetFilePathDto
import com.example.gym_mobile.Dto.LoginDto
import com.example.gym_mobile.Dto.SavePathDto
import com.example.gym_mobile.Entities.Exercise
import com.example.gym_mobile.Entities.Token
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserRepo {

    @POST("/user/uploadImageMobile")
    suspend fun uploadPath(@Body uploadPathDto: SavePathDto?)

    @GET("/user/getMobileImgPath/{id}")
    fun getFilePath(@Path("id") id: String?) : Call<GetFilePathDto>
}