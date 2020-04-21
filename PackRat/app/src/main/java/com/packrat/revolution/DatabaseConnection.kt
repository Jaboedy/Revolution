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



class DatabaseConnection {

    public  val rootURL  = "http://ec2-3-92-30-180.compute-1.amazonaws.com"
    private var Collections : List<Collection> = emptyList()
    private val unsafeHttpClient = getUnsafeOkHttpClient()

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

        //Return Your Unsafe OkHttp Client
        return OkHttpClient().newBuilder()
            .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier { _, _ -> true }.build()

    }

    fun SynchronousLogIn(userName: String, password: String) : String?{

        //Generate Request URL
        val url = "$rootURL/login?name=$userName&password=$password"

        //Generate the Request
        val request = Request.Builder().url(url).build()

        //Call Web Service
        val response = unsafeHttpClient.newCall(request).execute()

        //Return AuthToken
        return if (response.isSuccessful){
            response.body()?.toString()
        }else{
            null
        }
    }

    fun getCollections() : List<Collection>{
        return Collections
    }

    fun setCollections(AuthToken: String){

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
                Collections = gson.fromJson(body, Array<Collection>::class.java).toList()
            }
            override fun onFailure(call: Call, e: IOException) {
                println("Error: ${e.message}")
            }
        })
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

class DatabaseItem(val id: Int, val barcode: String, val name: String, val desc: String)
class DatabaseItemList(val DatabaseItems: List<DatabaseItem>)

