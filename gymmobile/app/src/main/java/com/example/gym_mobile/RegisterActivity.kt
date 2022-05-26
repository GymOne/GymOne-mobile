package com.example.gym_mobile

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.example.gym_mobile.Dto.RegisterDto
import com.example.gym_mobile.Repository.ApiConnector
import com.example.gym_mobile.Repository.AuthRepo
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        btnGoToLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        btnRegister.setOnClickListener {
            var registerDto = RegisterDto(registerNameInput.text.toString(), registerEmailInput.text.toString(), registerPasswordInput.text.toString() )

            val authAPi = ApiConnector.getInstance().create(AuthRepo::class.java)

            GlobalScope.launch {
                val result = authAPi.register(registerDto)
                if(result.isSuccessful){
                    goToLogin()
                }
            }
        }
    }

    fun goToLogin(){
        val intent = Intent(this, LoginActivity::class.java)
        intent.putExtra("loginData",registerEmailInput.text.toString())
        getResult.launch(intent)
    }

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) {}
}