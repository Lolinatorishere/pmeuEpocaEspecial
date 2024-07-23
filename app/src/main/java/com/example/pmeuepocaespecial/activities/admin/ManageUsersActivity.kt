package com.example.pmeuepocaespecial.activities.admin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pmeuepocaespecial.activities.user.ProfileActivity
import com.example.pmeuepocaespecial.adapters.UserAdapter
import com.example.pmeuepocaespecial.databinding.ActivityManageUsersBinding
import com.example.pmeuepocaespecial.datatypes.User
import com.example.pmeuepocaespecial.helpers.DatabaseHelper

class ManageUsersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManageUsersBinding
    var dbHelper = DatabaseHelper(this)
    private var projectId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityManageUsersBinding.inflate(layoutInflater)
        val project = dbHelper.getProject(projectId)
        setContentView(binding.root)

        initializeRecyclerView()

        if (project != null) {
            binding.projectTitle.text = project.title
        }

        binding.returnButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
      private fun initializeRecyclerView() {
        val usersList = fetchUsers()

        val userAdapter = UserAdapter(usersList, this)
        binding.tasks.apply {
            layoutManager = LinearLayoutManager(this@ManageUsersActivity)
            adapter = userAdapter
        }
    }

    private fun fetchUsers(): List<User> {
        val users: MutableList<User> = mutableListOf()
        users.addAll(dbHelper.getAllUsers())

        return users
    }
}

