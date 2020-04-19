package com.packrat.revolution

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.packrat.revolution.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(){


    private lateinit var binding: ActivityMainBinding
    private val user: User = User("Inconsequential Developer #1")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.user = user
    }




}
