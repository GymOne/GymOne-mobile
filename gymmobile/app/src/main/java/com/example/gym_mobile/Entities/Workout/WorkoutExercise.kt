package com.example.gym_mobile.Entities.Workout

import com.example.gym_mobile.Entities.Exercise

data class WorkoutExercise (
    val _id: String,
    val exercise: Exercise,
    val sets: List<WorkoutSet>,
)