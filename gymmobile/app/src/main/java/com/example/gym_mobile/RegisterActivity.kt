package com.example.gym_mobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gym_mobile.Services.AuthService
import com.example.gym_mobile.Services.LoginDto
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    val _authService = AuthService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        btnGoToLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        button.setOnClickListener {
            register()
        }
    }

    private fun register() {
        var registerDto = LoginDto(registerNameInput.text.toString(), registerEmailInput.text.toString(), registerPasswordInput.text.toString() )
        println(registerDto.name + registerDto.email + registerDto.password)
        _authService.register(registerDto, this)

    }

    fun openMainActivity(){
        val intent = Intent(this, MainActivity::class.java).apply {
        }
        startActivity(intent)
    }
}