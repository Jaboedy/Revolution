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
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import android.os.CountDownTimer
import android.se.omapi.Session
import androidx.databinding.adapters.TextViewBindingAdapter.setText




class DatabaseConnection {

    //Private Attributes
    private var Token          : String = ""
    private var Collections    : List<Collection> = emptyList()
    private var Items          : List<ItemG> = emptyList()
    private var ItemsByBarcode : List<ItemG> = emptyList()
    private var methodTimeOut :        Int = 15000
    private val unsafeHttpClient = getUnsafeOkHttpClient()

    //Public Attributes
    var collectionsAvailable :   Boolean = true
    var itemsAvailable:          Boolean = true
    var itemsByBarcodeAvailable: Boolean = true
    var tokenAvailable:          Boolean = true
    val rootURL  = "http://ec2-3-92-30-180.compute-1.amazonaws.com"

    fun getUnsafeOkHttpClient(): OkHttpClient {
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

        //Return Your Unsafe Highly Inefficient OkHttp Client
        val client = OkHttpClient().newBuilder()
                     .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                      .hostnameVerifier { _, _ -> true }.build()
        client.dispatcher().setMaxRequests(1)
        client.dispatcher().setMaxRequestsPerHost(1)
        return client

    }

    fun SynchronousLogIn(userName: String, password: String) : String?{

        //Block Token Request if Token Unavailable
        if (!tokenAvailable){
            return Token
        }

        //Set Availability
        else{
            tokenAvailable = false
        }

        //Generate Request URL
        val url = "$rootURL/login?name=$userName&password=$password"

        //Generate the Request
        val request = Request.Builder().url(url).build()

        //Call Web Service
        val response = unsafeHttpClient.newCall(request).execute()

        //Return AuthToken
        tokenAvailable = true
        return if (response.isSuccessful){
            response.body()?.toString()
        }else{
            null
        }
    }

    fun getAuthToken() : String{
        return Token
    }

    fun waitForAuthToken() : String{
        val endTime = System.currentTimeMillis() + methodTimeOut
        while (tokenAvailable == false && endTime > System.currentTimeMillis()){}
        return Token
    }

    fun setAuthToken(userName: String, password: String){
        //Block Token Request if Token Unavailable
        if (!tokenAvailable){
            return
        }

        //Set Availability
        else{
            tokenAvailable = false
        }

        //Generate Request URL
        val url = "$rootURL/login?name=$userName&password=$password"

        //Generate the Request
        val request = Request.Builder().url(url).build()

        unsafeHttpClient.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                if (body != null){
                   Token = body
                }
                tokenAvailable = true
            }
            override fun onFailure(call: Call, e: IOException) {
                println("Error: ${e.message}")
                tokenAvailable = true
            }
        })
    }

    fun createAuthToken(userName: String, password: String){

        //Block Token Request if Token Unavailable
        if (!tokenAvailable){
            return
        }

        //Set Availability
        else{
            tokenAvailable = false
        }

        //Generate Request URL
        val url = "$rootURL/user?name=$userName&password=$password"

        //Generate the Empty JSON Request Body
        val jsonMediaType = MediaType.parse("application/json; charset=utf-8")
        val jsonBody = "{}"
        val requestBody = RequestBody.create(jsonMediaType, jsonBody)

        //Generate the Request
        val request = Request.Builder().url(url).put(requestBody).build()

        unsafeHttpClient.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                if (body != null){
                    Token = body
                }
                tokenAvailable = true
            }
            override fun onFailure(call: Call, e: IOException) {
                println("Error: ${e.message}")
                tokenAvailable = true
            }
        })
    }

    fun getCollections() : List<Collection>{
        return Collections
    }

    fun waitForCollections() : List<Collection>{
        val endTime = System.currentTimeMillis() + methodTimeOut
        while (collectionsAvailable == false && endTime > System.currentTimeMillis()){}
        return Collections
    }

    fun setCollections(AuthToken: String){

        //Block Token Request if Token Unavailable
        if (!collectionsAvailable){
            return
        }

        //Set Availability
        else{
            collectionsAvailable = false
        }

        //Generate the URL
        val url = "$rootURL/collection?token=$AuthToken"

        //Generate the Request
        val request = Request.Builder().url(url).get().build()

        //Generate the Client
        val client = getUnsafeOkHttpClient()


        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                 val body = response.body()?.string()
                println("Code: ${response.code()} | Body: $body")
                if (body != null){
                    val gson = GsonBuilder().create()
                    Collections = gson.fromJson(body, Array<Collection>::class.java).toList()
                }
                collectionsAvailable = true
            }
            override fun onFailure(call: Call, e: IOException) {
                println("Error: ${e.message}")
                collectionsAvailable = true
            }
        })
    }

    fun addToCollection(name: String, desc: String){

    }

    fun createCollection(){

    }

    fun getItems() : List<ItemG>{
        return Items
    }

    fun setItems(AuthToken: String, CollectionId : String){

        //Block Token Request if Token Unavailable
        if (!itemsAvailable){
            return
        }

        //Set Availability
        else{
            itemsAvailable = false
        }

        //Generate the URL
        val url = "$rootURL/collection?token=$AuthToken"

        //Generate the Request
        val request = Request.Builder().url(url).get().build()

        //Generate the Client
        val client = getUnsafeOkHttpClient()


        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                println("Code: ${response.code()} | Body: $body")
                val gson = GsonBuilder().create()
                Items = gson.fromJson(body, Array<ItemG>::class.java).toList()
                itemsAvailable = true
            }
            override fun onFailure(call: Call, e: IOException) {
                println("Error: ${e.message}")
                itemsAvailable = true
            }
        })
    }

    fun createItems(){

    }

    /*
    fun CreateUser(userName: String, password: String){

        //Print Line for Function Initiation Confirmation
        println("Attempting to Create User")

        //Generate the Empty JSON Request Body
        val JSON = MediaType.parse("application/json; charset=utf-8")
        val jsonBody = "{}"
        val RequestBody = RequestBody.create(JSON, jsonBody)

        //Generate the Request
        val url = "http://ec2-3-92-30-180.compute-1.amazonaws.com/user?name=$userName&password=$password"
        val request = Request.Builder().url(url).put(RequestBody).build()
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

    fun AddItemToDatabase(Barcode: String, Name: String, Description: String){

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
    fun GetItemFromDatabase(Barcode: String) : List<DatabaseItem>{

        //Print Line for Function Initiation Confirmation
        println("Attempting to Get Item to the Database")
        val result = ArrayList<DatabaseItem>()

        //Generate the Request
        val url = "http://ec2-3-92-30-180.compute-1.amazonaws.com/item?barcode=$Barcode"
        val request = Request.Builder().url(url).get().build()
        val client = getUnsafeOkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                println("Code: ${response.code()} | Body: $body")
                val gson = GsonBuilder().create()
                val databaseItemList : List<DatabaseItem> = gson.fromJson(body, Array<DatabaseItem>::class.java).toList()
                for (DatabaseItem in databaseItemList)
                    result.add(DatabaseItem)
            }
            override fun onFailure(call: Call, e: IOException) {
                println("Error: ${e.message}")
            }
        })
        return result

    }

     */
}

class CollectionFeed(val Collections: List<Collection>)
class Collection(val id: Int, val user_id: Int, val name: String, val desc: String)

class ItemFeed(val DatabaseItems: List<ItemG>)
class ItemG(val id: Int, val barcode: String, val name: String, val desc: String)
