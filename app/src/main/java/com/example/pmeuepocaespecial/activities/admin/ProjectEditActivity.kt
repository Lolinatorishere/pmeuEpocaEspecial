package com.example.pmeuepocaespecial.activities.admin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.example.pmeuepocaespecial.R
import com.example.pmeuepocaespecial.activities.user.MainActivity
import com.example.pmeuepocaespecial.databinding.ActivityEditProjectBinding
import com.example.pmeuepocaespecial.datatypes.Project
import com.example.pmeuepocaespecial.helpers.DatabaseHelper

class ProjectEditActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityEditProjectBinding
    var dbHelper = DatabaseHelper(this)
    private var projectId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProjectBinding.inflate(layoutInflater)
        projectId = intent.getIntExtra("projectId", 0)


        setContentView(binding.root)

        setEditHints(projectId)
        val project = dbHelper.getProject(projectId)

        binding.btnManageTasks.setOnClickListener {
            val intent = Intent(this, ProjectManageTasksActivity::class.java)
            intent.putExtra("projectId", projectId)
            startActivity(intent)
            finish()
        }

        binding.btnManageUsers.setOnClickListener {
            val intent = Intent(this, ProjectManageUsersActivity::class.java)
            intent.putExtra("projectId", projectId)
            startActivity(intent)
            finish()
        }

        if (project != null) {
            if(project.status == 1){
                binding.btnSetStatusReOpen.visibility = View.GONE
                binding.btnSetStatusComplete.visibility = View.VISIBLE
            }else{
                binding.btnSetStatusReOpen.visibility = View.VISIBLE
                binding.btnSetStatusComplete.visibility = View.GONE
            }
        }else{
            binding.btnSetStatusReOpen.visibility = View.VISIBLE
            binding.btnSetStatusComplete.visibility = View.GONE
        }


        binding.btnDeleteProject.setOnClickListener {
            val message = dbHelper.deleteProject(projectId, this)
            Toast.makeText(this, message.returnString, Toast.LENGTH_SHORT).show()
            if(message.returnBoolean){
                finish()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }


        binding.btnSetStatusComplete.setOnClickListener {
            changeStatus(0)
            finish()
            startActivity(intent)
        }

        binding.btnSetStatusReOpen.setOnClickListener {
            changeStatus(1)
            finish()
            startActivity(intent)
        }

        binding.returnButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            finish()
            startActivity(intent)
        }

        // Create Project Button OnClickListener
        binding.editButton.setOnClickListener {
            if(editProject()){
                finish()
                startActivity(intent)
            }
        }
    }


    private fun setEditHints(projectId: Int) {
        // Example: Fetch and display the username
        // You'll need to implement fetching user info from shared preferences or database
        val project = dbHelper.getProject(projectId)
        project?.let{
            binding.addProjectName.hint = getString(R.string.title) + " " + it.title
            binding.addProjectCategory.hint = getString(R.string.category)+ ": " + it.category
            binding.addProjectDescription.hint = getString(R.string.description) + " " + it.description

        }
    }

    // Example function to handle project creation logic
    private fun editProject(): Boolean {
        // Collect input data
        val projectName = binding.addProjectName.text.toString().trim()
        val projectCategory = binding.addProjectCategory.text.toString().trim()
        val projectDescription = binding.addProjectDescription.text.toString().trim()
        val editProject = Project(0, projectName, projectDescription, "", 1, projectCategory)

        // Validate inputs (simple validation example)
        if(!dbHelper.checkTitleUniqueness(projectName)){
            Toast.makeText(this, R.string.create_project_title_non_unique, Toast.LENGTH_SHORT).show()
            return false
        }
        dbHelper.updateProject(editProject, projectId, this)

        // Show confirmation message
        Toast.makeText(this, R.string.edit_project_success, Toast.LENGTH_SHORT).show()

        // Optional: Clear input fields for a new entry or navigate to another activity
        binding.addProjectName.text.clear()
        binding.addProjectCategory.text.clear()
        binding.addProjectDescription.text.clear()
        return true
    }
    private fun changeStatus(state: Int){
        val editProject = Project(0,"","", "", state ,"")
        dbHelper.updateProject(editProject, projectId, this)
    }
}