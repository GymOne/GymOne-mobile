package com.example.gym_mobile.Model

import com.example.gym_mobile.Dto.SubmitFriendRequestDto
import com.example.gym_mobile.Entities.Exercise
import com.example.gym_mobile.Repository.ApiConnector
import com.example.gym_mobile.Repository.FriendsRepo

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