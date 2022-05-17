package com.example.gym_mobile.Model

import com.example.gym_mobile.Entities.Exercise

object DataStor{

    val arrayList = ArrayList<Exercise>()//Creating an empty arraylist
    init {

    }

    fun addExcercise(exercise: Exercise) {
        arrayList.add(exercise)
    }

    fun removeExcercise(exercise: Exercise) {
        arrayList.remove(exercise)
    }
}