package com.example.pmeuepocaespecial.activities.user

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.pmeuepocaespecial.R
import com.example.pmeuepocaespecial.databinding.ActivityEditProfileBinding
import com.example.pmeuepocaespecial.datatypes.User
import com.example.pmeuepocaespecial.helpers.UserAuthHelper
import com.example.pmeuepocaespecial.helpers.DatabaseHelper
import com.example.pmeuepocaespecial.helpers.ImageHelper

class UserEditActivity : AppCompatActivity() {
    private var imageByteArray: ByteArray = byteArrayOf()
    private lateinit var binding: ActivityEditProfileBinding
    private val loginIntent = Intent(this, LoginActivity::class.java)
    private val profileEditIntent = Intent(this, ProfileEditActivity::class.java)
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


    private var userAuth = UserAuthHelper(this)
    private var dbHelper = DatabaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {

        if(!userAuth.checkLoginStatus()) startActivity(loginIntent)

        super.onCreate(savedInstanceState)

        val sharedUserId = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val userId = sharedUserId.getInt("userId", 0)

        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        binding.returnButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent);
            finish()
        }

        binding.registerButton.setOnClickListener {
            displayInputInfo(userId)
            finish()
            startActivity(intent)
        }
        binding.btnSelectImage.setOnClickListener {
            pickImageResultLauncher.launch("image/*")
        }
        setContentView(binding.root)

        displayUserInfo(userId)


    }

    private fun displayInputInfo(userId: Int){
        val usernameInput = binding.username.text.toString().trim()
        val nameInput = binding.name.text.toString().trim()
        val emailInput = binding.email.text.toString().trim()
        var pass = binding.password.text.toString().trim()
        val confirm = binding.confirmPassword.text.toString().trim()
        val pfp = imageByteArray

        if (confirm != pass){
            pass = ""
        }

        val user = User(0, nameInput, usernameInput, pfp, emailInput, pass, null)

        dbHelper.updateUser(user, userId, this)

    }

    private fun displayUserInfo(userId: Int) {
        // Example: Fetch and display the username
        // You'll need to implement fetching user info from shared preferences or database
        var user = dbHelper.getUserPublic(userId)
        val byteArray: ByteArray = user?.pfp ?: ByteArray(0)
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        var userLevel = R.string.user_permission_user
        var userText = getString(R.string.user_permission_user)
        if (user != null) {
            if(user.userPermission == 1){
               userText = getString(R.string.user_permission_admin)
            }
        }
        user?.let{
            if(bitmap == null || bitmap.width == 0 || bitmap.height == 0){
                binding.imagePreview.setImageResource(R.mipmap.ic_launcher)
            }else{
                binding.imagePreview.setImageBitmap(bitmap)
            }
            binding.username.hint = getString(R.string.username) + ": " + it.username
            binding.name.hint = getString(R.string.name_string)+ ": " + it.name
            binding.email.hint = getString(R.string.email) + ": " + it.email
        }
    }
}