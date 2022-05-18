package com.example.gym_mobile.Entities.Workout

import java.io.Serializable

data class WorkoutSet (
    val _id: String,
    val weight: Int,
    val reps: Int,
) : Serializable