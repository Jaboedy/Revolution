package com.packrat.revolution

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.packrat.revolution.databinding.ActivityMainBinding
import okhttp3.*
import java.io.IOException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class MainActivity : AppCompatActivity(){


    private lateinit var binding: ActivityMainBinding
    private val user: User = User("Inconsequential Developer #1")

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        DatabaseConnection.LogIn("CoronaVirus", "DownTheEconomy")
        binding.user = user
    }





}
