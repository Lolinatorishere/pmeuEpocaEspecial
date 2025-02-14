package com.example.pmeuepocaespecial.activities.user

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pmeuepocaespecial.R
import com.example.pmeuepocaespecial.activities.admin.AddProjectActivity
import com.example.pmeuepocaespecial.adapters.ProjectAdapter
import com.example.pmeuepocaespecial.databinding.ActivityMainBinding
import com.example.pmeuepocaespecial.datatypes.Project
import com.example.pmeuepocaespecial.helpers.DatabaseHelper
import com.example.pmeuepocaespecial.helpers.ImageHelper
import com.example.pmeuepocaespecial.helpers.UserAuthHelper

class MainActivity : AppCompatActivity() {
    private val userAuth = UserAuthHelper(this)
    private val dbHelper = DatabaseHelper(this);
    private val imgHelper = ImageHelper(this);
    private val loginIntent = Intent("com.example.ACTION_LOGOUT")

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkUserLoggedIn()

        val sharedUserId = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val user = dbHelper.getUser(sharedUserId.getInt("userId", 0))

        if(user != null){
            binding.profileUsername.text = user.username
            if(user.pfp == null){
                binding.profileImageView.setImageResource(R.mipmap.ic_launcher)
            }else{
                binding.profileImageView.setImageBitmap(imgHelper.decodeByteArrayToImage(user.pfp))
            }
        }else{
            userAuth.logout()
        }

        initializeRecyclerView()

        if(!userAuth.checkUserAuth()){
            binding.addButton.visibility = View.GONE
        }else{
            binding.addButton.visibility = View.VISIBLE
        }

        binding.addButton.setOnClickListener {
            if (user != null) {
                if(user.userPermission == 1){
                    val intent = Intent(this, AddProjectActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    Toast.makeText(this, R.string.permission_error, Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "user is null", Toast.LENGTH_SHORT).show()
            }
        }

        binding.profileImageView.setOnClickListener{
            val profileIntent = Intent(this, ProfileActivity::class.java)
            startActivity(profileIntent)
            finish()
        }

    }

    // Fetch user information and display it
   private fun initializeRecyclerView() {
        val projectsList = fetchProjects()

        val projectAdapter = ProjectAdapter(projectsList, this)
        binding.projects.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = projectAdapter
        }
    }

    private fun checkUserLoggedIn() {
        if (!userAuth.checkLoginStatus()) {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
        }
    }

    private fun fetchProjects(): List<Project> {
        val projects: MutableList<Project> = mutableListOf()
        projects.addAll(dbHelper.getAllProjects())
        return projects
    }
}