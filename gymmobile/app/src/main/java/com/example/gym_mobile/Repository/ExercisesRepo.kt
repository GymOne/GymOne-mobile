package com.example.gym_mobile.Repository

import com.example.gym_mobile.Dto.CreateExerciseDto
import com.example.gym_mobile.Entities.Exercise
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ExercisesRepo {
    @GET("/exercise/findByUserId/{id}")
    fun getExercisesByUserId(@Path("id") id: String?) : Call<List<Exercise>>

    @DELETE("/exercise/deleteById/{id}")
    suspend fun deleteExerciseById(@Path("id") id: String):Response<Any>

    @POST("/exercise/create")
    suspend fun createExercise(@Body createExerciseDto: CreateExerciseDto):Response<Any>
}