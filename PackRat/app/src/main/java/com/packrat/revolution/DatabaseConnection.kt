package com.packrat.revolution

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




object DatabaseConnection {
    private fun getUnsafeOkHttpClient(): OkHttpClient {
        //Create a trust manager that does not validate certificate chains
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun getAcceptedIssuers() = arrayOf<X509Certificate>()
        })

        //Install the all-trusting free hugs loving trust manager
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())

        //Create an SSL socket factory with our all-trusting free hugs loving manager
        val sslSocketFactory = sslContext.socketFactory

        return OkHttpClient().newBuilder().sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier { _, _ -> true }.build()

    }

    public fun LogIn(userName: String, password: String){
        println("Attempting to Log In")
        val url = "http://ec2-3-92-30-180.compute-1.amazonaws.com/login?name=$userName&password=$password"
        val request = Request.Builder().url(url).build()
        val client = getUnsafeOkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                println("Code: ${response.code()} | Body: ${response.body()?.string()}")
            }
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to Execute!!!")
            }
        })
    }

    public fun CreateUser(userName: String, password: String){
        println("Attempting to Create User")
        val url = "http://ec2-3-92-30-180.compute-1.amazonaws.com/user?name=$userName&password=$password"
        val requestBody = MultipartBody.Builder()
                         .setType(MultipartBody.FORM)
                         .addFormDataPart("someParam", "someValue")
                         .build()
        val request = Request.Builder().url(url).put(requestBody).build()
        val client = getUnsafeOkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                println("Code: ${response.code()} | Body: ${response.body()?.string()}")
            }
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to Execute!!!")
            }
        })
    }

    public fun AddItemToDatabase(Barcode: String, Name: String, Description: String){

        //Print Line for Function Initiation Confirmation
        println("Attempting to Add Item to the Database")

        //Generate the JSON Request Body
        val JSON = MediaType.parse("application/json; charset=utf-8")
        val jsonBody = "{\"barcode\": \"$Barcode\",\"name\": \"$Name\",\"desc\": \"$Description\"}"
        val body = RequestBody.create(JSON, jsonBody)

        //Generate the Request
        val url = "http://ec2-3-92-30-180.compute-1.amazonaws.com/item"
        val request = Request.Builder().url(url).put(body).build()
        val client = getUnsafeOkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                println("Code: ${response.code()} | Body: ${response.body()?.string()}")
            }
            override fun onFailure(call: Call, e: IOException) {
                println("Error: ${e.message}")
            }
        })
    }
}