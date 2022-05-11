package com.example.gym_mobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gym_mobile.Services.AuthService
import com.example.gym_mobile.Services.LoginDto
import kotlinx.android.synthetic.main.activity_login.*

const val EXTRA_MESSAGE = "token_from_login"

class LoginActivity : AppCompatActivity() {

    val _authService = AuthService()

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
        var loginDto = LoginDto(loginEmailInput.text.toString(), loginPasswordInput.text.toString() )
        _authService.login(this, loginDto)

    }

    fun openMainActivity(token: String){
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, token)
        }
        startActivity(intent)
    }
}