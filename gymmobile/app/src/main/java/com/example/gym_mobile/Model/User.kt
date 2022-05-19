package com.example.gym_mobile.Model

import android.util.Log
import com.auth0.android.jwt.JWT
import com.example.gym_mobile.Entities.User

object User {
    private var token: String? = null
    private var userId:String? = null
    private var userEmail:String? = null

    fun getToken() = token

    fun getUserId() = userId

    fun getUserEmail() = userEmail

//    fun getUser():User?{
//        val jwt = JWT(token!!)
//        val user = jwt.getClaim("user").asObject(User::class.java)
//        return user;
//    }

    fun setToken(token:String){
        this.token = token
        setUserId()
    }

    fun setUserId(){
        val jwt = JWT(token!!)
        userId = jwt.getClaim("user").asObject(User::class.java)?.id
        userEmail = jwt.getClaim("user").asObject(User::class.java)?.email
    }

    fun removeToken(){
        token = null
    }
}