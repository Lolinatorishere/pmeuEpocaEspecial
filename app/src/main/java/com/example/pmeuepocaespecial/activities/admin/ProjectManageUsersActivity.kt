package com.example.pmeuepocaespecial.activities.admin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pmeuepocaespecial.activities.user.ProjectUserActivity
import com.example.pmeuepocaespecial.adapters.UserProjectAdapter
import com.example.pmeuepocaespecial.databinding.ActivityManageProjectUsersBinding
import com.example.pmeuepocaespecial.datatypes.User
import com.example.pmeuepocaespecial.helpers.DatabaseHelper

class ProjectManageUsersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManageProjectUsersBinding
    var dbHelper = DatabaseHelper(this)
    private var projectId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityManageProjectUsersBinding.inflate(layoutInflater)
        projectId = intent.getIntExtra("projectId", 0)

        setContentView(binding.root)

        initializeRecyclerView()

        binding.returnButton.setOnClickListener {
            val intent = Intent(this, ProjectEditActivity::class.java)
            intent.putExtra("projectId", projectId)
            startActivity(intent)
            finish()
        }

        binding.addButton.setOnClickListener{
            val intent = Intent(this, ProjectUserActivity::class.java)
            intent.putExtra("projectId", projectId)
            startActivity(intent)
            finish()
        }
    }
      private fun initializeRecyclerView() {
        val usersList = fetchUsers()

        val taskAdapter = UserProjectAdapter(usersList, this, projectId)
        binding.users.apply {
            layoutManager = LinearLayoutManager(this@ProjectManageUsersActivity)
            adapter = taskAdapter
        }
    }

    private fun fetchUsers(): List<User> {
        val users: MutableList<User> = mutableListOf()
        users.addAll(dbHelper.getProjectUsers(projectId))
        return users
    }
}

