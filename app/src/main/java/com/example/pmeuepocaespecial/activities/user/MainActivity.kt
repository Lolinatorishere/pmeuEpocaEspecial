package com.example.pmeuepocaespecial.activities.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pmeuepocaespecial.activities.admin.AddProjectActivity
import com.example.pmeuepocaespecial.databinding.ActivityMainBinding
import com.example.pmeuepocaespecial.helpers.UserAuthHelper

class MainActivity : AppCompatActivity() {
    val userAuth = UserAuthHelper(this)
    val loginIntent = Intent("com.example.lab001.SEG")

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        if(!userAuth.checkLoginStatus()) startActivity(loginIntent)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addButton.setOnClickListener {
            val intent = Intent(this, AddProjectActivity::class.java)
            startActivity(intent)
        }

    }
}