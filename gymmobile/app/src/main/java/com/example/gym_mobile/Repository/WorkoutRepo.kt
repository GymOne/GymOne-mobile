package com.example.gym_mobile.Repository

import com.example.gym_mobile.Dto.CreateWorkoutExerciseDto
import com.example.gym_mobile.Dto.CreateWorkoutExerciseSetDto
import com.example.gym_mobile.Dto.CreateWorkoutSessionDto
import com.example.gym_mobile.Entities.Workout.WorkoutExercise
import com.example.gym_mobile.Entities.Workout.WorkoutSession
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface WorkoutRepo {

    @GET("/workout/session/{userId}/{date}")
    fun getWorkoutSession(@Path("userId") userId: String?, @Path("date") date: String) : Call<WorkoutSession>

    @POST("/workout/session")
    suspend fun createWorkoutSession(@Body createWorkoutSessionDto: CreateWorkoutSessionDto?) : Response<Any>

    @DELETE("/workout/exercise/set/deleteById/{id}")
    suspend fun deleteWorkoutExerciseSet(@Path("id") id: String) : Response<Any>

    @DELETE("/workout/exercise/deleteById/{id}")
    suspend fun deleteWorkoutExercise(@Path("id") id: String) : Response<Any>

    @POST("/workout/exercise/set")
    suspend fun createWorkoutExerciseSet(@Body createWorkoutExerciseSetDto: CreateWorkoutExerciseSetDto) : Response<Any>

    @POST("/workout/exercise")
    suspend fun createWorkoutExercise(@Body createWorkoutExerciseDto: CreateWorkoutExerciseDto) : Response<Any>

    @GET("/workout/exercise/getById/{id}")
    fun getWorkoutExercise(@Path("id") id: String) : Call<WorkoutExercise>
}