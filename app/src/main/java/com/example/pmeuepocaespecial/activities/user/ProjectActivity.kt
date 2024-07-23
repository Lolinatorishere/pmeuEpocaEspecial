package com.example.pmeuepocaespecial.activities.user

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pmeuepocaespecial.R
import com.example.pmeuepocaespecial.activities.admin.ProjectEditActivity
import com.example.pmeuepocaespecial.adapters.TaskAdapter
import com.example.pmeuepocaespecial.adapters.UserProjectAdapter
import com.example.pmeuepocaespecial.databinding.ActivityProjectBinding
import com.example.pmeuepocaespecial.datatypes.Task
import com.example.pmeuepocaespecial.datatypes.User
import com.example.pmeuepocaespecial.helpers.UserAuthHelper
import com.example.pmeuepocaespecial.helpers.DatabaseHelper

class ProjectActivity : AppCompatActivity() {
    private var projectId: Int = 0
    private lateinit var binding: ActivityProjectBinding
    private val loginIntent = Intent(this, LoginActivity::class.java)
    private val taskIntent = Intent(this, ProjectTaskActivity::class.java)
    private val projectEditIntent = Intent(this, ProjectEditActivity::class.java)

    private var userAuth = UserAuthHelper(this)
    private var dbHelper = DatabaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(!userAuth.checkLoginStatus()) startActivity(loginIntent)

        binding = ActivityProjectBinding.inflate(layoutInflater)
        projectId = intent.getIntExtra("projectId", 0)

        val userAdmin = userAuth.checkUserAuth()
        val projectAuth = userAuth.checkProjectAuth(projectId)

        initializeTaskRecyclerView()

        initializeUserRecyclerView()

        setContentView(binding.root)

        displayInfo(projectId)

        binding.btnShowTasks.setOnClickListener {
            binding.tasks.visibility = View.VISIBLE
            binding.users.visibility = View.GONE
        }

        binding.btnShowTasks.setOnClickListener {
            binding.tasks.visibility = View.GONE
            binding.users.visibility = View.VISIBLE
        }

        binding.btnReturnToMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent);
            finish()
        }
        if(!userAdmin){
            if(!projectAuth){
                binding.btnAlterProject.visibility = View.GONE
            }
        }else{
            binding.btnAlterProject.visibility = View.VISIBLE
        }
        binding.btnAlterProject.setOnClickListener {
            if(!userAdmin){
                if(!projectAuth){
                    Toast.makeText(this, R.string.no_authorisation , Toast.LENGTH_SHORT).show()
                }
            }else{
                val intent = Intent(this, ProjectEditActivity::class.java)
                intent.putExtra("projectId", projectId)
                startActivity(intent);
                finish()
            }
        }
    }

    private fun displayInfo(projectId: Int) {
        var status = "default"
        val project = dbHelper.getProject(projectId)

        if (project != null) {
            status = if(project.status != 1 ){
                getString(R.string.status_inactive)
            }else{
                getString(R.string.status_active)
            }
        }
        project?.let{
            binding.projectTitleContent.text = it.title
            binding.projectStatusContent.text = status
            binding.projectDate.text = it.date
            binding.categoryContent.text = it.category
            binding.descriptionContent.text = it.description
        }
    }
    private fun initializeTaskRecyclerView() {
        val taskList = fetchTasks()

        val projectAdapter = TaskAdapter(taskList, this)

        binding.tasks.adapter = projectAdapter
        binding.tasks.layoutManager = LinearLayoutManager(this)
    }

    private fun fetchTasks(): List<Task> {
        val tasks: MutableList<Task> = mutableListOf()
        tasks.addAll(dbHelper.getProjectTasks(projectId))
        return tasks
    }

    private fun initializeUserRecyclerView() {
        val userList = fetchUsers()

        val projectAdapter = UserProjectAdapter(userList, this, projectId)

        binding.tasks.adapter = projectAdapter
        binding.tasks.layoutManager = LinearLayoutManager(this)
    }

    private fun fetchUsers(): List<User> {
        val tasks: MutableList<User> = mutableListOf()
        tasks.addAll(dbHelper.getProjectUsers(projectId))
        return tasks
    }
}