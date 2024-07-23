package com.example.pmeuepocaespecial.activities.user

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pmeuepocaespecial.adapters.TaskAdapter
import com.example.pmeuepocaespecial.databinding.ActivityViewProjectTasksBinding
import com.example.pmeuepocaespecial.datatypes.Task
import com.example.pmeuepocaespecial.helpers.DatabaseHelper

class ProjectViewTasksActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewProjectTasksBinding
    var dbHelper = DatabaseHelper(this)
    private var projectId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityViewProjectTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)
        projectId = intent.getIntExtra("projectId", 0)
        val project = dbHelper.getProject(projectId)
        setContentView(binding.root)

        initializeRecyclerView()

        if (project != null) {
            binding.projectTitle.text = project.title
        }

        binding.returnButton.setOnClickListener {
            val intent = Intent(this, ProjectActivity::class.java)
            intent.putExtra("projectId", projectId)
            startActivity(intent)
            finish()
        }

    }
      private fun initializeRecyclerView() {
        val tasksList = fetchTasks()

        val taskAdapter = TaskAdapter(tasksList, this, projectId)
        binding.tasks.apply {
            layoutManager = LinearLayoutManager(this@ProjectViewTasksActivity)
            adapter = taskAdapter
        }
    }

    private fun fetchTasks(): List<Task> {
        val projects: MutableList<Task> = mutableListOf()
        projects.addAll(dbHelper.getProjectTasks(projectId))

        return projects
    }
}

