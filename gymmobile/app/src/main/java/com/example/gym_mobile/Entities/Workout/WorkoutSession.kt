package com.example.gym_mobile.Entities.Workout

import java.util.*

data class WorkoutSession (
    val _id: String,
    val userId: String,
    val workouts: List<WorkoutExercise>,
    val date: Date,
)