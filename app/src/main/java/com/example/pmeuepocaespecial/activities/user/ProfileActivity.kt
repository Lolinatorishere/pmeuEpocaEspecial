package com.example.pmeuepocaespecial.activities.user

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pmeuepocaespecial.R
import com.example.pmeuepocaespecial.databinding.ActivityProfileBinding
import com.example.pmeuepocaespecial.helpers.UserAuthHelper
import com.example.pmeuepocaespecial.helpers.DatabaseHelper

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val loginIntent = Intent(this, LoginActivity::class.java)
    private val mainIntent = Intent(this, MainActivity::class.java)
    private val profileEditIntent = Intent(this, ProfileEditActivity::class.java)

    private var userAuth = UserAuthHelper(this)
    private var dbHelper = DatabaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        if(!userAuth.checkLoginStatus()) startActivity(loginIntent)
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        binding.btnReturnToMain.setOnClickListener {
            startActivity(mainIntent);
        }
        binding.btnAlterProfile.setOnClickListener {
            startActivity(profileEditIntent);
        }
        setContentView(binding.root)

        val sharedUserId = getSharedPreferences("UserIdPrefs", Context.MODE_PRIVATE)
        val userId = sharedUserId.getInt("UserId", 0)

        // Fetch user information and display it
        displayUserInfo(userId)

    }

    private fun displayUserInfo(userId: Int) {
        // Example: Fetch and display the username
        // You'll need to implement fetching user info from shared preferences or database
        var user = dbHelper.getUserPublic(userId)
        val byteArray: ByteArray = user?.pfp ?: ByteArray(0)
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        var userLevel = R.string.user_permission_user
        if (user != null) {
            if(user.userPermission == 1){
               userLevel = R.string.user_permission_admin
            }
        }
        user?.let{
            binding.profileImageView.setImageBitmap(bitmap)
            binding.usernameTextView.text = it.username
            binding.nameTextView.text = it.name
            binding.emailTextView.text = it.email
            binding.permissionTextView.text = it.email
        }
        // Set other user information similarly
    }
}