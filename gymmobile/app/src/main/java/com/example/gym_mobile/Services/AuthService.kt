package com.example.gym_mobile.Services
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.gym_mobile.LoginActivity
import com.example.gym_mobile.RegisterActivity
import com.example.gym_mobile.Services.Dtos.LoginDto
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class AuthService {
    val baseUrl = "http://10.0.2.2:3000/"
    val loginUrl = "auth/login"
    val registerUrl = "auth/register"
    val loginDtoInstance = LoginDto("", "")

    fun login(activity: LoginActivity, loginDto: LoginDto) {
        val jsonBody = JSONObject(Gson().toJson(loginDto))
        val request = JsonObjectRequest(Request.Method.POST,baseUrl+loginUrl,jsonBody,
            { response ->
                try {
                    val apiResponse = Gson().fromJson(response.toString(), loginDtoInstance.javaClass)
                    activity.openMainActivity()
                }catch (e:Exception){
                    println(e)
                }
            }, {
                println("Volley error: $it")
            })
        val que = Volley.newRequestQueue(activity)
        que.add(request)
    }

    fun register(registerDto: LoginDto, activity: RegisterActivity){
        val jObject = JSONObject(Gson().toJson(registerDto))

        val request = JsonObjectRequest(Request.Method.POST,baseUrl+registerUrl,jObject,
            { response ->
                try {
                    val apiResponse = Gson().fromJson(response.toString(), loginDtoInstance.javaClass)
                    activity.openMainActivity()
                }catch (e:Exception){
                    println(e)
                }
            }, {
                println("Volley error: $it")
            })
        val que = Volley.newRequestQueue(activity)
        que.add(request)
    }

}