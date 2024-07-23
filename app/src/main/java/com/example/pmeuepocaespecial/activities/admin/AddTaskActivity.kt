package com.example.pmeuepocaespecial.activities.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pmeuepocaespecial.R
import com.example.pmeuepocaespecial.activities.user.MainActivity
import com.example.pmeuepocaespecial.databinding.ActivityAddTaskBinding
import com.example.pmeuepocaespecial.datatypes.Task
import com.example.pmeuepocaespecial.helpers.DatabaseHelper

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding
    var dbHelper = DatabaseHelper(this)
    private var projectId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        projectId = intent.getIntExtra("projectId", 0)
        setContentView(binding.root)

        // Return Button OnClickListener
        binding.returnButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Create Project Button OnClickListener
        binding.createButton.setOnClickListener {
            // Example implementation
            if(createTask()){
                val intent = Intent(this, ProjectManageTasksActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    // Example function to handle project creation logic
    private fun createTask(): Boolean {
        // Collect input data
        val taskName = binding.addProjectName.text.toString().trim()
        val taskCategory = binding.addProjectCategory.text.toString().trim()
        val taskDescription = binding.addProjectDescription.text.toString().trim()
        val newTask = Task(0, taskName, taskDescription, "", "", 0, 1, taskCategory, projectId)

        // Validate inputs (simple validation example)
        if (taskName.isEmpty() || taskCategory.isEmpty() || taskDescription.isEmpty()) {
            Toast.makeText(this, R.string.create_project_no_input, Toast.LENGTH_SHORT).show()
            return false
        }
        dbHelper.insertTask(newTask, this)

        // Show confirmation message
        Toast.makeText(this, R.string.create_project_success, Toast.LENGTH_SHORT).show()

        // Optional: Clear input fields for a new entry or navigate to another activity
        binding.addProjectName.text.clear()
        binding.addProjectCategory.text.clear()
        binding.addProjectDescription.text.clear()
        return true
    }
}