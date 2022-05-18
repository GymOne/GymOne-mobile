package com.example.gym_mobile.Entities.Workout

import com.example.gym_mobile.Entities.Exercise
import java.io.Serializable

data class WorkoutExercise (
    val _id: String,
    val exercise: Exercise,
    val sets: List<WorkoutSet>,
) :Serializable