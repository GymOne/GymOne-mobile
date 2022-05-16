package com.example.gym_mobile.Repository

import com.example.gym_mobile.Entities.Exercise
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ExercisesRepo {
    @GET("/exercise/findByUserId/{id}")
    fun getExercisesByUserId(@Path("id") id: String?) : Call<List<Exercise>>
}