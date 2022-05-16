package com.example.gym_mobile.Repository

import com.example.gym_mobile.Dto.LoginDto
import com.example.gym_mobile.Dto.RegisterDto
import com.example.gym_mobile.Entities.Token
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthRepo {

    @POST("auth/login")
    suspend fun login(@Body loginDto: LoginDto?) : Response<Token>

    @POST("auth/register")
    suspend fun register(@Body registerDto: RegisterDto?) : Response<Any>
}