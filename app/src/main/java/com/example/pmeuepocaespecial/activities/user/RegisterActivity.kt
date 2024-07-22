package com.example.pmeuepocaespecial.activities.user

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.pmeuepocaespecial.R
import com.example.pmeuepocaespecial.datatypes.User
import com.example.pmeuepocaespecial.datatypes.functionStringReturn
import com.example.pmeuepocaespecial.helpers.DatabaseHelper
import com.example.pmeuepocaespecial.helpers.ImageHelper

class RegisterActivity : AppCompatActivity() {
    private var imageByteArray: ByteArray = byteArrayOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        val loginIntent = Intent("com.example.lab001.SEG")
        val pickImageResultLauncher = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->
           if (uri != null) {
               val imgHelp = ImageHelper(this)
               imageByteArray = imgHelp.encodeImageToByteArray(uri, this)
               val imageView = findViewById<ImageView>(R.id.imagePreview)
               imageView.setImageURI(uri);
           }
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val nameEditText = findViewById<EditText>(R.id.name)
        val usernameEditText = findViewById<EditText>(R.id.username)
        val emailEditText = findViewById<EditText>(R.id.email)
        val passwordEditText = findViewById<EditText>(R.id.password)
        val confirmPasswordEditText = findViewById<EditText>(R.id.confirmPassword)
        val returnButton = findViewById<Button>(R.id.returnButton)
        val registerButton = findViewById<Button>(R.id.registerButton)
        val selectImageButton = findViewById<Button>(R.id.btnSelectImage)


        returnButton.setOnClickListener {
            startActivity(loginIntent)
            finish()
        }

        registerButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val username = usernameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val confirmPassword = confirmPasswordEditText.text.toString().trim()
            val image = imageByteArray
            val isValid = checkUserInput(name, username, email, password, confirmPassword, image)
            if(isValid.returnBoolean){
                val dbHelper = DatabaseHelper(this)
                val user = User(0, name, username, image, email, password, 0)
                val userValid = dbHelper.insertUser(user, this)
                if(userValid.returnBoolean){
                    Toast.makeText(this, userValid.returnString, Toast.LENGTH_SHORT).show()
                    startActivity(loginIntent)
                    finish()
                }else{
                    Toast.makeText(this, userValid.returnString ,Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, isValid.returnString, Toast.LENGTH_SHORT).show()
            }

        }

        selectImageButton.setOnClickListener {
            pickImageResultLauncher.launch("image/*")
        }
    }
   private fun checkUserInput(name: String, username: String, email: String, password: String, confirmPassword: String, image: ByteArray): functionStringReturn{
       val dbHelper = DatabaseHelper(this)
       if(name.isEmpty()){
           return functionStringReturn(this.getString(R.string.register_error_empty_name), false)
       }

       if(username.isEmpty()){
           return functionStringReturn(this.getString(R.string.register_error_empty_username), false)
       }

       if(email.isEmpty()){
           return functionStringReturn(this.getString(R.string.register_error_empty_email), false)
       }

       if(password.isEmpty()){
           return functionStringReturn(this.getString(R.string.register_error_empty_password), false)
       }

       if(confirmPassword.isEmpty()){
           return functionStringReturn(this.getString(R.string.register_error_empty_confirmpassword), false)
       }

       if(image.isEmpty()){
           return functionStringReturn(this.getString(R.string.register_error_empty_image), false)
       }
       if(password != confirmPassword){
           return functionStringReturn(this.getString(R.string.confirm_password_error), false)
       }

       return functionStringReturn("Valid", true);
   }
}