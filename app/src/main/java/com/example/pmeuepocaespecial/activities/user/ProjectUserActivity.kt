package com.example.pmeuepocaespecial.activities.user

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pmeuepocaespecial.R
import com.example.pmeuepocaespecial.activities.admin.ProjectUserEditActivity
import com.example.pmeuepocaespecial.databinding.ActivityProjectUserBinding
import com.example.pmeuepocaespecial.helpers.UserAuthHelper
import com.example.pmeuepocaespecial.helpers.DatabaseHelper

class ProjectUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProjectUserBinding
    private val loginIntent = Intent(this, LoginActivity::class.java)
    private val projectUserEditIntent = Intent(this, ProjectUserEditActivity::class.java)

    private var userAuth = UserAuthHelper(this)
    private var dbHelper = DatabaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {

        if(!userAuth.checkLoginStatus()) startActivity(loginIntent)

        super.onCreate(savedInstanceState)
        binding = ActivityProjectUserBinding.inflate(layoutInflater)
        binding.btnReturnToMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent);
            finish()
        }
        binding.btnAlterUserProject.setOnClickListener {
            startActivity(projectUserEditIntent);
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
            binding.projectTitleContent.text = it.username
            binding.projectStatusContent.text = it.name
            binding.userTypeContent.text = it.email
        }
    }
}