package com.example.gym_mobile.Dto

data class SubmitFriendRequestDto (
    val senderId: String,
    val receiverId: String,
    val isAccepted: Boolean
)