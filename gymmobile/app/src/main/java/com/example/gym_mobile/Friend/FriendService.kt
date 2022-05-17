package com.example.gym_mobile.Friend

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

class FriendService {
    val baseUrl = "http://10.0.2.2:3000/friend/getRequestsByEmail/"
    val loginUrl = "auth/login"
    val friendDto: FriendDto = FriendDto("", "", true)


    fun getFriendsByEmail(testEmail: String, context: Context?) {
//        val jsonBody = JSONObject(Gson().toJson())
        val request = StringRequest(
            Request.Method.GET,baseUrl+testEmail, Response.Listener<String>
            { response ->
                println(response)
                    val typeofList = object : TypeToken<List<FriendDto>>() {}.type
                    val friendList = Gson().fromJson<List<FriendDto>>(response, typeofList)
                return@Listener
                    println(friendList)
            }, {
                println("Volley error: $it")
            })
        val que = Volley.newRequestQueue(context)
        que.add(request)
    }
}