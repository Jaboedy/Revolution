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
    private var Token              : String = ""
    private var Collections        : List<Collection> = emptyList()
    private var ItemsInCollection  : List<ItemG> = emptyList()
    private var ItemsByBarcode     : List<ItemG> = emptyList()
    private var AddedItemsByBarcode: List<ItemA> = emptyList()
    private var methodTimeOut     : Int = 15000
    private val unsafeHttpClient = getUnsafeOkHttpClient()

    //Public Attributes: Locks
    var tokenAvailable:                   Boolean = true
    var collectionsAvailable :            Boolean = true
    var createdCollectionAvailable:       Boolean = true
    var itemsInCollectionAvailable:       Boolean = true
    var addedItemsInCollectionAvailable : Boolean = true
    var itemsByBarcodeAvailable:          Boolean = true
    var addedItemsByBarcodeAvailable:     Boolean = true

    //Public Attributes: Return Messages
    var tokenReturnMessage: String = ""
    var collectionReturnMessage: String = ""
    var addItemByBarcodeReturnMessage: String = ""


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

    /*Functions for AuthToken Actions*/
    fun waitForAuthToken() : String{
        val endTime = System.currentTimeMillis() + methodTimeOut
        while (tokenAvailable == false && endTime > System.currentTimeMillis()){}
        return Token
    }

    fun getAuthToken() : String{
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
                val responseData = response
                val responseBody = responseData.body()?.string()
                if (responseData.code() == 200){
                   tokenReturnMessage = "Log In Successful!"
                   if (responseBody != null){
                       Token = responseBody
                   }
                }
                if (responseData.code() == 401){
                    tokenReturnMessage = "Invalid Username and/or Password."
                }
                println("Token: $Token" + "Return Message: $tokenReturnMessage" )
                tokenAvailable = true
            }
            override fun onFailure(call: Call, e: IOException) {
                println("Error: ${e.message}")
                tokenReturnMessage = "Log In Failed. Please Try Again Later!"
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

    /*Functions for Collection Actions*/
    fun waitForCollections() : List<Collection>{
        val endTime = System.currentTimeMillis() + methodTimeOut
        while (collectionsAvailable == false && endTime > System.currentTimeMillis()){}
        return Collections
    }

    fun getCollections() : List<Collection>{
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
                val responseData = response
                val body = responseData.body()?.string()
                println("Code: ${response.code()} | Body: $body")
                if (responseData.code() == 400){
                    collectionReturnMessage = "An Error Has Occurred. Please Try Again Later!"
                }
                if (responseData.code() == 200){
                    val gson = GsonBuilder().create()
                    Collections = gson.fromJson(body, Array<Collection>::class.java).toList()
                    collectionReturnMessage = "Collection Retrieval Successful"
                }
                collectionsAvailable = true
            }
            override fun onFailure(call: Call, e: IOException) {
                println("Error: ${e.message}")
                collectionsAvailable = true
            }
        })
    }

    fun createCollection(AuthToken: String, name: String, desc: String){
        //Block Token Request if Token Unavailable
        if (!createdCollectionAvailable){
            return
        }

        //Set Availability
        else{
            createdCollectionAvailable = false
        }


        //Generate Request URL
        val url = "$rootURL/collection?token=$AuthToken"

        //Generate the JSON Request Body
        val jsonMediaType = MediaType.parse("application/json; charset=utf-8")
        val jsonBody = "{\"name\": \"$name\",\"desc\": \"$desc\"}"
        val requestBody = RequestBody.create(jsonMediaType, jsonBody)

        //Generate the Request
        val request = Request.Builder().url(url).put(requestBody).build()

        unsafeHttpClient.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                if (body != null){
                    println("Success: $body")
                }
                createdCollectionAvailable = true
            }
            override fun onFailure(call: Call, e: IOException) {
                println("Error: ${e.message}")
                createdCollectionAvailable = true
            }
        })
    }

    /*Functions for Items in Collection Actions*/
    fun waitForItemsInCollection(): List<ItemG>{
        val endTime = System.currentTimeMillis() + methodTimeOut
        while (itemsInCollectionAvailable == false && endTime > System.currentTimeMillis()){}
        return ItemsInCollection
    }

    fun getItemsInCollection() : List<ItemG>{
        return ItemsInCollection
    }

    fun setItemsInCollection(AuthToken: String, CollectionId : String){

        //Block Token Request if Token Unavailable
        if (!itemsInCollectionAvailable){
            return
        }

        //Set Availability
        else{
            itemsInCollectionAvailable = false
        }

        //Generate the URL
        val url = "$rootURL/collection_item?token=$AuthToken&collection_id=$CollectionId"

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
                    ItemsInCollection = gson.fromJson(body, Array<ItemG>::class.java).toList()
                }
                itemsInCollectionAvailable = true
            }
            override fun onFailure(call: Call, e: IOException) {
                println("Error: ${e.message}")
                itemsInCollectionAvailable = true
            }
        })
    }

    fun addItemsInCollection(AuthToken: String, CollectionId: String, ItemId: String){
        //Block Token Request if Token Unavailable
        if (!addedItemsInCollectionAvailable){
            return
        }

        //Set Availability
        else{
            addedItemsInCollectionAvailable = false
        }

        //Generate Request URL
        val url = "$rootURL/collection_item?token=$AuthToken&collection_id=$CollectionId&item_id=$ItemId"

        //Generate the JSON Request Body
        val jsonMediaType = MediaType.parse("application/json; charset=utf-8")
        val jsonBody = "{}"
        val requestBody = RequestBody.create(jsonMediaType, jsonBody)

        //Generate the Request
        val request = Request.Builder().url(url).put(requestBody).build()

        unsafeHttpClient.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                println("Code: ${response.code()} | Body: $body")
                addedItemsInCollectionAvailable = true
            }
            override fun onFailure(call: Call, e: IOException) {
                println("Error: ${e.message}")
                addedItemsInCollectionAvailable = true
            }
        })
    }

    /*Functions for Items by Barcode Actions*/
    fun waitForItemsByBarcode(): List<ItemG>{
        val endTime = System.currentTimeMillis() + methodTimeOut
        while (itemsByBarcodeAvailable == false && endTime > System.currentTimeMillis()){}
        return ItemsByBarcode
    }

    fun getItemsByBarcode() : List<ItemG>{
        return ItemsByBarcode
    }

    fun setItemsByBarcode(barcode: String){

        //Block Token Request if Token Unavailable
        if (!itemsByBarcodeAvailable){
            return
        }

        //Set Availability
        else{
            itemsByBarcodeAvailable = false
        }

        //Generate the URL
        val url = "$rootURL/item?barcode=$barcode"

        //Generate the Request
        val request = Request.Builder().url(url).build()

        unsafeHttpClient.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                println("Code: ${response.code()} | Body: $body")
                if (body != null){
                    val gson = GsonBuilder().create()
                    ItemsByBarcode = gson.fromJson(body, Array<ItemG>::class.java).toList()
                }
                itemsByBarcodeAvailable = true
            }
            override fun onFailure(call: Call, e: IOException) {
                println("Error: ${e.message}")
                itemsByBarcodeAvailable = true
            }
        })
    }

    fun waitForAddedItemsByBarcode() : List<ItemA>{
        val endTime = System.currentTimeMillis() + methodTimeOut
        while (addedItemsByBarcodeAvailable == false && endTime > System.currentTimeMillis()){}
        return AddedItemsByBarcode
    }

    fun getAddedItemsByBarcode() : List<ItemA>{
        return AddedItemsByBarcode
    }

    fun addItemsByBarcode(Barcode: String, Name: String, Description: String){
        //Block Token Request if Token Unavailable
        if (!addedItemsByBarcodeAvailable){
            return
        }

        //Set Availability
        else{
            addedItemsByBarcodeAvailable = false
        }

        //Generate the JSON Request Body
        val jsonMediaType = MediaType.parse("application/json; charset=utf-8")
        val jsonBody = "{\"barcode\": \"$Barcode\",\"name\": \"$Name\",\"desc\": \"$Description\"}"
        val requestBody = RequestBody.create(jsonMediaType, jsonBody)

        //Generate the URL
        val url = "$rootURL/item"


        //Generate the Request
        val request = Request.Builder().url(url).put(requestBody).build()

        unsafeHttpClient.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseData = response
                val body = responseData.body()?.string()
                println("Code: ${response.code()} | Body: $body")
                if(responseData.code() == 409){
                    addItemByBarcodeReturnMessage = "This Item Already Exsists!"
                }
                if (responseData.code() == 201){
                    if (body != null){
                        val gson = GsonBuilder().create()
                        AddedItemsByBarcode = gson.fromJson(body, Array<ItemA>::class.java).toList()
                        addItemByBarcodeReturnMessage = "This Item Has Been Created!"
                    }
                }
                addedItemsByBarcodeAvailable = true
            }
            override fun onFailure(call: Call, e: IOException) {
                println("Error: ${e.message}")
                addItemByBarcodeReturnMessage = "An Error Has Occurred! Please Try Again Later!!!"
                addedItemsByBarcodeAvailable = true
            }
        })

    }

}

class CollectionFeed(val Collections: List<Collection>)
class Collection(val id: Int, val user_id: Int, val name: String, val desc: String)

class ItemFeed(val DatabaseItems: List<ItemG>)
class ItemG(val id: Int, val barcode: String, val name: String, val desc: String)
class ItemA(val id: Int)
