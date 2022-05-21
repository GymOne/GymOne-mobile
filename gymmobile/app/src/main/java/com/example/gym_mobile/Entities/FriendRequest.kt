package com.example.gym_mobile.Entities

import java.io.Serializable

data class FriendRequest(
    val id: String,
    val name: String,
    val email: String
) : Serializable
