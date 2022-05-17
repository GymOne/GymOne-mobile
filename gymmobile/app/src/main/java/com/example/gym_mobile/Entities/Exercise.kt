package com.example.gym_mobile.Entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Exercise(
        val _id: String,
        val name: String
    ): Parcelable
