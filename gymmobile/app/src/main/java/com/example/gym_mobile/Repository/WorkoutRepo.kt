package com.example.gym_mobile.Repository

import com.example.gym_mobile.Entities.Exercise
import com.example.gym_mobile.Entities.Workout.WorkoutSession
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WorkoutRepo {

    @GET("/workout/session/{userId}/{date}")
    fun getWorkoutSession(@Path("userId") userId: String?, @Path("date") date: String) : Call<WorkoutSession>
}