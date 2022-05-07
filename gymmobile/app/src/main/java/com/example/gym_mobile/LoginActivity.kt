package com.example.gym_mobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gym_mobile.Services.AuthService
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btnLogin.setOnClickListener {
            httpRequest()
        }
    }

    fun httpRequest(){
        var _authService =  AuthService()
        _authService.apiCall(this)
    }

}