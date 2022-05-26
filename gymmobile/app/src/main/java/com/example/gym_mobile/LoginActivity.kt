package com.example.gym_mobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gym_mobile.Dto.LoginDto
import com.example.gym_mobile.Model.User
import com.example.gym_mobile.Repository.ApiConnector
import com.example.gym_mobile.Repository.AuthRepo
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

const val EXTRA_MESSAGE = "token_from_login"

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val email = intent.getSerializableExtra("loginData") as? String

        loginEmailInput.setText(email)

        btnLogin.setOnClickListener {
            var loginDto = LoginDto(loginEmailInput.text.toString(), loginPasswordInput.text.toString() )
            val authAPi = ApiConnector.getInstance().create(AuthRepo::class.java)

            GlobalScope.launch {
                val result = authAPi.login(loginDto)
                if(result.isSuccessful){
                    result.body()?.let { User.setToken(it.token) }
                    openMainActivity()
                }
            }
        }

        btnGoToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    fun openMainActivity(){
        startActivity(Intent(this, MainActivity::class.java).apply {})
    }
}