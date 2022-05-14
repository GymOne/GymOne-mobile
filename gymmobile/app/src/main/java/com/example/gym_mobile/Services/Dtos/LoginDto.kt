package com.example.gym_mobile.Services.Dtos

class LoginDto(var email: String, var password: String, var name: String?, var jwtToken: String?){

    //Login
        constructor(email: String, password: String) : this(email, password, null, null)

    //Register extra
        constructor(email: String, password: String, name: String) : this(email, password, name, null){}


}