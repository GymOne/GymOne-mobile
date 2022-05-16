package com.example.gym_mobile.Model

import android.util.Log
import com.auth0.android.jwt.JWT
import com.example.gym_mobile.Entities.User

object User {
    private var token: String? = null;

    fun getToken() = token

    fun getUser():User?{
        val jwt = JWT(token!!)
        val user = jwt.getClaim("user").asObject(User::class.java)
        return user;
    }

    fun setToken(token:String){
        this.token = token
    }

    fun removeToken(){
        token = null
    }
}