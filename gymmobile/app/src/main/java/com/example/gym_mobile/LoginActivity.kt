package com.example.gym_mobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gym_mobile.Services.AuthService
import com.example.gym_mobile.Services.LoginDto
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin.setOnClickListener {
            getConnectionAuth()
        }

        btnGoToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun getConnectionAuth() {
        var auth = AuthService();
        var mb = LoginDto("dfv", "djhfbj")
        auth.apiCall(this, welcomeText)
    }
}