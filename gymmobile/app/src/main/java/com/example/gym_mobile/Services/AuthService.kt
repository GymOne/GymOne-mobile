package com.example.gym_mobile.Services
import android.util.Log
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.gym_mobile.LoginActivity
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONException
import org.json.JSONObject

class AuthService {
    //val url = "https://pastebin.com/raw/2bW31yqa"
    val url = "http://10.0.2.2:3000/friend/getRequestsByEmail/sender%40g.com"

    fun apiCall(mainActivity: LoginActivity, textView: TextView) {






        val request = JsonObjectRequest(
            Request.Method.GET, // method
            url, // url
            null, // json request
            { response -> // response listener

                try {
                    val obj: JSONObject = response
                    val array = obj.getJSONArray("students")

                    textView.text = ""

                    // loop through the array elements
                    for (i in 0 until  array.length()){
                        // get current json object as student instance
                        val student: JSONObject = array.getJSONObject(i)

                        // get the current student (json object) data
                        val id: String = student.getString("_id")
                        val sender: String = student.getString("senderId")
                        val receiver: String = student.getString("receiverId")

                        //display the formatted json data in text view
                        textView.append("$id $sender\nage : $receiver\n\n")
                    }

                }catch (e: JSONException){
                    textView.text = e.message
                }

            },
            { error -> // error listener
                textView.text = error.message
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

}