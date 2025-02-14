package com.example.pmeuepocaespecial.activities.admin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pmeuepocaespecial.adapters.TaskProjectAdapter
import com.example.pmeuepocaespecial.databinding.ActivityManageProjectTasksBinding
import com.example.pmeuepocaespecial.datatypes.Task
import com.example.pmeuepocaespecial.helpers.DatabaseHelper

class ProjectManageTasksActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManageProjectTasksBinding
    var dbHelper = DatabaseHelper(this)
    private var projectId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityManageProjectTasksBinding.inflate(layoutInflater)
        projectId = intent.getIntExtra("projectId", 0)
        val project = dbHelper.getProject(projectId)
        setContentView(binding.root)

        initializeRecyclerView()

        if (project != null) {
            binding.projectTitle.text = project.title
        }

        binding.returnButton.setOnClickListener {
            val intent = Intent(this, ProjectEditActivity::class.java)
            intent.putExtra("projectId", projectId)
            startActivity(intent)
            finish()
        }

        binding.addButton.setOnClickListener{
            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putExtra("projectId", projectId)
            startActivity(intent)
            finish()
        }
    }
      private fun initializeRecyclerView() {
        val tasksList = fetchTasks()

        val taskAdapter = TaskProjectAdapter(tasksList, this, projectId)
        binding.tasks.apply {
            layoutManager = LinearLayoutManager(this@ProjectManageTasksActivity)
            adapter = taskAdapter
        }
    }

    private fun fetchTasks(): List<Task> {
        val projects: MutableList<Task> = mutableListOf()
        projects.addAll(dbHelper.getProjectTasks(projectId))

        return projects
    }
}


