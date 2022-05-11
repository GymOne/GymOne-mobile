package com.example.gym_mobile.Services
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.gym_mobile.LoginActivity
import com.example.gym_mobile.RegisterActivity
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class AuthService {
    //val url = "https://pastebin.com/raw/2bW31yqa"
    val url = "http://10.0.2.2:3000/friend/getRequestsByEmail/sender@g.com"
    val loginUrl = "http://10.0.2.2:3000/auth/login"
    val registerUrl = "http://10.0.2.2:3000/auth/register"

    fun apiCall(mainActivity: LoginActivity, loginDto: LoginDto) {

        val request = JsonArrayRequest(
            Request.Method.POST, // method
            loginUrl, // url
            null, // json request
            { response -> // response listener
                try {
                    println(response)
                    val obj: JSONArray = response
                    // loop through the array elements
                    for (i in 0 until  obj.length()){
                        // get current json object as student instance
                        val student: JSONObject = obj.getJSONObject(i)

                        // get the current student (json object) data
                        val id: String = student.getString("_id")
                        val sender: String = student.getString("senderId")
                        val receiver: String = student.getString("receiverId")

                    }

                }catch (e: JSONException){
                    println(e)
                }

            },
            { error -> // error listener
                println(error)
            }
        )

        // add network request to volley queue
        //VolleySingleton.getInstance(applicationContext)
          //  .addToRequestQueue(request)


        val que = Volley.newRequestQueue(mainActivity)
        que.add(request)




        //      val que = Volley.newRequestQueue(mainActivity)
//        val jsonObjectRequest = JsonObjectRequest(
//            Request.Method.GET, url, null, {
//                Log.d("What are we getting from the api:   " ,"Hello there")
//            }, {
//                Log.d("oooopse", "fucky waky")
//            }
//        )

//        val jsonReq = JsonObjectRequest(
//            "https://www.freeforexapi.com/api/live?pairs=EURUSD",
//            Response.Listener<JSONObject>(),
//        )

//  This is working with following request
        // val url = "https://www.google.com"
//        val stringRequest = StringRequest(Request.Method.GET, url,
//        Response.Listener<String> { response ->
//            // Display the first 500 characters of the response string.
//            welcomeText.text = "Response is: ${response.substring(0, 500)}"
//        },
//        Response.ErrorListener { welcomeText.text = "That didn't work!" })
        //que.add(stringRequest)
    }

    fun login(activity: LoginActivity, loginDto: LoginDto) : String{
        //val json: String = Gson().toJson(loginDto)
        val jsonBody = JSONObject()
        jsonBody.put("email", loginDto.email)
        jsonBody.put("password", loginDto.password)



        val request = JsonObjectRequest(Request.Method.POST,loginUrl,jsonBody,
            { response ->
                // Process the json
                try {
                    activity.openMainActivity(NotProud(response))
                    //var responseObject =  JSON.parse
                }catch (e:Exception){
                    println(e)
                }

            }, {
                // Error in request
                println("Volley error: $it")
            })

        println("Before requestQue")
        val que = Volley.newRequestQueue(activity)
        que.add(request)
        return "test"
        //val request: JsonObjectRequest = JsonObjectRequest(Request.Method.POST ,loginUrl, jsonBody, )
    }

    private fun NotProud(response: JSONObject?) : String {
        var list = response.toString().split(":")
        var tokenWithQuotation = list.get(1)
        var finalToken: String  = tokenWithQuotation.substring(1, tokenWithQuotation.length - 1).dropLast(1);
        return finalToken
    }

    fun register(registerDto: LoginDto, activity: RegisterActivity){
        println("In auth service")
        val jsonBody = JSONObject()
        jsonBody.put("name", registerDto.name)
        jsonBody.put("email", registerDto.email)
        jsonBody.put("password", registerDto.password)



        val request = JsonObjectRequest(Request.Method.POST,registerUrl,jsonBody,
            { response ->
                // Process the json
                try {
                    println(response)
                    //activity.openMainActivity(NotProud(response))
                    //var responseObject =  JSON.parse
                }catch (e:Exception){
                    println(e)
                }

            }, {
                // Error in request
                println("Volley error: $it")
            })

        println("Before requestQue")
        val que = Volley.newRequestQueue(activity)
        que.add(request)
    }

}