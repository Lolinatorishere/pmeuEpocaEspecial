package com.example.pmeuepocaespecial.activities.user

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pmeuepocaespecial.R
import com.example.pmeuepocaespecial.helpers.UserAuthHelper

class LoginActivity : AppCompatActivity() {
    private lateinit var logoutReciever: BroadcastReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val registerIntent = Intent("com.example.lab002.SEG")

        val usernameEditText = findViewById<EditText>(R.id.username)
        val passwordEditText = findViewById<EditText>(R.id.password)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerButton = findViewById<Button>(R.id.registerButton)

        logoutReciever = object : BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                finish()
                startActivity(Intent(context, LoginActivity::class.java))
            }
        }

        registerReceiver(logoutReciever, IntentFilter("com.example.ACTION_LOGOUT"))

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val userLogin = UserAuthHelper(this)
            if(username.isEmpty()){
                Toast.makeText(this , R.string.login_error_empty_username, Toast.LENGTH_SHORT).show()
            }
            if(password.isEmpty()){
                Toast.makeText(this , R.string.login_error_empty_password, Toast.LENGTH_SHORT).show()
            }
            if(password.isNotEmpty() && username.isNotEmpty()){
                if(userLogin.login(username, password)){
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }else {
                    Toast.makeText(this, R.string.error_invalid_credentials, Toast.LENGTH_SHORT).show()
                }
            }
        }
        registerButton.setOnClickListener{
            startActivity(registerIntent)
            finish()
        }
    }

    override fun onDestroy(){
        super.onDestroy()
        unregisterReceiver(logoutReciever)
    }
}


