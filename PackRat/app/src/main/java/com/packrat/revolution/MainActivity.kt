package com.packrat.revolution

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.packrat.revolution.databinding.ActivityMainBinding
import com.packrat.revolution.FetchCompleteListener
import okhttp3.*
import java.io.IOException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity(){


    private lateinit var binding: ActivityMainBinding
    val DatabaseTest = DatabaseConnection()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        DatabaseTest.setAuthToken("COVID1","SponsoredByFoxNews")
        val authToken = DatabaseTest.waitForAuthToken()
        val user = User("Inconsequential Developer #1", authToken)
        DatabaseTest.setCollections(authToken)
        while (!DatabaseTest.collectionsAvailable){}
        val Collections = DatabaseTest.getCollections()
    }


/*
    fun logIn(userName: String, password: String, debugOn: Boolean){

        //Print Line for Function Initiation Confirmation
        if (debugOn){
            println("Attempting to Log In")
        }

        //Variable Declaration
        var client = getUnsafeOkHttpClient()
        var request= OkHttpRequest(client)

        //Generate Request URL
        val url = "${request.rootURL}/login?name=$userName&password=$password"

        //Get Main UI Handeler
        request.GET(url, object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                val code = response.code()
                runOnUiThread {
                    try {
                        if (debugOn) {
                            println("Code: $code | Body: $body")
                        }
                        if(body != null){
                            val jsonResponse = JSONObject("{authToken: \"$body\"}")
                            responsePayloadJSON = jsonResponse
                            responsePayloadJSON.put("valid",true)
                            this@MainActivity.fetchComplete()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()

                    }

                }
            }
            override fun onFailure(call: Call, e: IOException) {
                if (debugOn){
                    println("Error: ${e.message}")
                }
            }
        })
    }
*/
    //Brody Implement This Function for Setting Data
    //Look at this link for Reference: https://github.com/RohanJahagirdar/Out-Of-Eden/blob/master/app/src/main/java/me/rohanjahagirdar/outofeden/Chapter/ChapterActivity.kt
   /* override fun fetchComplete() {
        println("fetchCOmplete:   " + responsePayloadJSON)
        if(responsePayloadJSON.getBoolean("valid")) {
            val breakpointvariable = responsePayloadJSON

        }
    }*/



}
