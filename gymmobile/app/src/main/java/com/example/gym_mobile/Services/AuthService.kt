package com.example.gym_mobile.Services

import android.app.DownloadManager
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.gym_mobile.MainActivity

class AuthService {
    val url = "http://localhost:3000/friend/getRequestsByEmail/receiver%40g.com"

    fun apiCall(mainActivity: MainActivity) {
        val que = Volley.newRequestQueue(mainActivity)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null, Response.Listener {
                Log.d("What are we getting from the api:   " ,"Hello there")
            },Response.ErrorListener {
                Log.d("oooopse", "fucky waky")
            }
        )
        que.add(jsonObjectRequest)
    }

}