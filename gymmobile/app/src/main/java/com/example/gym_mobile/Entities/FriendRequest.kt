package com.example.gym_mobile.Entities

import java.io.Serializable

data class FriendRequest(
    val senderId: String,
    val receiverId: String,
    val isAccepted: Boolean
) : Serializable
