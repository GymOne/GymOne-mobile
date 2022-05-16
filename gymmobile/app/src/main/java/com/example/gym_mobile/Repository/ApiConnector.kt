package com.example.gym_mobile.Repository

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConnector {
    val baseUrl = "http://10.0.2.2:3000/"

    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}}