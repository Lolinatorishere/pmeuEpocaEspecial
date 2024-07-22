package com.example.pmeuepocaespecial.activities.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.example.pmeuepocaespecial.R
import com.example.pmeuepocaespecial.activities.user.MainActivity
import com.example.pmeuepocaespecial.databinding.ActivityAddProjectBinding
import com.example.pmeuepocaespecial.datatypes.Project
import com.example.pmeuepocaespecial.helpers.DatabaseHelper

class AddProjectActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityAddProjectBinding
    var dbHelper = DatabaseHelper(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Return Button OnClickListener
        binding.returnButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Create Project Button OnClickListener
        binding.registerButton.setOnClickListener {
            // Example implementation
            if(createProject()){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    // Example function to handle project creation logic
    private fun createProject(): Boolean {
        // Collect input data
        val projectName = binding.addProjectName.text.toString().trim()
        val projectCategory = binding.addProjectCategory.text.toString().trim()
        val projectDescription = binding.addProjectDescription.text.toString().trim()
        val newProject = Project(0, projectName, projectDescription, "", 1, projectCategory)

        // Validate inputs (simple validation example)
        if (projectName.isEmpty() || projectCategory.isEmpty() || projectDescription.isEmpty()) {
            Toast.makeText(this, R.string.create_project_no_input, Toast.LENGTH_SHORT).show()
            return false
        }
        if(!dbHelper.checkTitleUniqueness(projectName)){
            Toast.makeText(this, R.string.create_project_title_non_unique, Toast.LENGTH_SHORT).show()
            return false
        }
        dbHelper.insertProject(newProject)

        // Show confirmation message
        Toast.makeText(this, R.string.create_project_success, Toast.LENGTH_SHORT).show()

        // Optional: Clear input fields for a new entry or navigate to another activity
        binding.addProjectName.text.clear()
        binding.addProjectCategory.text.clear()
        binding.addProjectDescription.text.clear()
        return true
    }
}