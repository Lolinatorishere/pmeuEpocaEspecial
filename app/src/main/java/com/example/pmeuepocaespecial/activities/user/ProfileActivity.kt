package com.example.pmeuepocaespecial.activities.user

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.pmeuepocaespecial.R
import com.example.pmeuepocaespecial.activities.admin.UserEditActivity
import com.example.pmeuepocaespecial.databinding.ActivityProfileBinding
import com.example.pmeuepocaespecial.helpers.UserAuthHelper
import com.example.pmeuepocaespecial.helpers.DatabaseHelper

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val loginIntent = Intent(this, LoginActivity::class.java)
    private val userEditIntent = Intent("com.example.USER_UPDATE")

    private var userAuth = UserAuthHelper(this)
    private var dbHelper = DatabaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {

        if(!userAuth.checkLoginStatus()) startActivity(loginIntent)

        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        binding.btnReturnToMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent);
            finish()
        }

        if(!userAuth.checkUserAuth()){
            binding.btnAlterProfile.visibility = View.GONE
        }else{
            binding.btnAlterProfile.visibility = View.VISIBLE
        }

        binding.btnAlterProfile.setOnClickListener {
            startActivity(userEditIntent);
            finish()
        }

        binding.btnLogout.setOnClickListener {
            userAuth.logout()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        setContentView(binding.root)

        val sharedUserId = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val userId = sharedUserId.getInt("userId", 0)

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
        var userText = getString(R.string.user_permission_user)
        if (user != null) {
            if(user.userPermission == 1){
               userText = getString(R.string.user_permission_admin)
            }
        }
        user?.let{
            binding.profileImageView.setImageBitmap(bitmap)
            binding.usernameTextView.text = it.username
            binding.nameTextView.text = it.name
            binding.emailTextView.text = it.email
            binding.permissionTextView.text = userText
        }
    }
}