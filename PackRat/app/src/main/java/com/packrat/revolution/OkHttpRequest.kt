package com.packrat.revolution

import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import okhttp3.OkHttpClient
import android.R.string
import android.os.Looper
import java.util.logging.Handler


class OkHttpRequest(client: OkHttpClient) {
    val rootURL  = "http://ec2-3-92-30-180.compute-1.amazonaws.com"
    private var client = OkHttpClient()

    init {
        this.client = client
    }

    fun GET(url: String, callback: Callback): Call {
        val request = Request.Builder() .url(url).build()
        val call = client.newCall(request)
        call.enqueue(callback)
        return call
    }

    fun PUT(url: String, jsonBody: String, callback: Callback): Call{

        //Generate the JSON Request Body
        val json = MediaType.parse("application/json; charset=utf-8")
        val body = RequestBody.create(json, jsonBody)

        //Generate the Request
        val request = Request.Builder().url(url).put(body).build()

        //Enqueue the Call
        val call = client.newCall(request)
        call.enqueue(callback)
        return call
    }

    fun CreateUser(userName: String, password: String, debugOn: Boolean) : Call {

        //Print Line for Function Initiation Confirmation
        if (debugOn){
            println("Attempting to Create User")
        }

        //Generate Request URL
        val url = "$rootURL/user?name=$userName&password=$password"

        //Generate JSONRequest Body
        val jsonBody = "{}"

        //Get Main UI Handeler
        return PUT(url, jsonBody, object: Callback {
            override fun onResponse(call: Call, response: Response) {

                val body = response.body()?.string()
                val code = response.code()
                if (debugOn) {
                    println("Code: $code | Body: $body")
                }
                //Function to Set View for Collections Screen
            }
            override fun onFailure(call: Call, e: IOException) {
                if (debugOn){
                    println("Error: ${e.message}")
                }
            }
        })
    }

}