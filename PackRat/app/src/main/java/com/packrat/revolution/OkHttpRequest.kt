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

class OkHttpRequest {
    private var client = getUnsafeOkHttpClient()
    private var rootURL = "http://ec2-3-92-30-180.compute-1.amazonaws.com"

    private fun getUnsafeOkHttpClient(): OkHttpClient {
        //Create a trust manager that does not validate certificate chains
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }
            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }
            override fun getAcceptedIssuers() = arrayOf<X509Certificate>()
        })

        //Install the all-trusting free hugs loving trust manager
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())

        //Create an SSL socket factory with our all-trusting free hugs loving manager
        val sslSocketFactory = sslContext.socketFactory

        //Return Your Unsafe OkHttp Client
        return OkHttpClient().newBuilder()
                             .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                             .hostnameVerifier { _, _ -> true }.build()

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

    fun CreateUser(userName: String, password: String, debugOn: Boolean) {

        //Print Line for Function Initiation Confirmation
        if (debugOn){
            println("Attempting to Create User")
        }

        //Generate Request URL
        val url = "$rootURL/user?name=$userName&password=$password"

        //Generate JSONRequest Body
        val jsonBody = "{}"

        //Get Main UI Handeler
        PUT(url, jsonBody, object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                val code = response.code()
                if (debugOn) {
                    println("Code: $code | Body: $body")
                }
                //Function to Set Authtoken for User
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