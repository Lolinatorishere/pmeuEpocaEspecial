package com.example.pmeuepocaespecial.activities.user

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pmeuepocaespecial.R
import com.example.pmeuepocaespecial.activities.admin.ProjectEditActivity
import com.example.pmeuepocaespecial.databinding.ActivityProjectBinding
import com.example.pmeuepocaespecial.helpers.UserAuthHelper
import com.example.pmeuepocaespecial.helpers.DatabaseHelper

class ProjectActivity : AppCompatActivity() {
    private var projectId: Int = 0
    private lateinit var binding: ActivityProjectBinding
    private val loginIntent = Intent("android.intent.action.MAIN")
    private val taskIntent = Intent(this, ProjectTaskActivity::class.java)
    private val projectEditIntent = Intent(this, ProjectEditActivity::class.java)

    private var userAuth = UserAuthHelper(this)
    private var dbHelper = DatabaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {

        if(!userAuth.checkLoginStatus()) startActivity(loginIntent)

        binding = ActivityProjectBinding.inflate(layoutInflater)
        projectId = intent.getIntExtra("projectId", 0)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project)
        binding.btnReturnToMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent);
            finish()
        }
        binding.btnAlterProject.setOnClickListener {
            startActivity(projectEditIntent);
            finish()
        }
        // Fetch user information and display it
        displayInfo(projectId)

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
            binding.projectStatus.text = status
            binding.projectDate.text = it.date
            binding.categoryContent.text = it.category
            binding.descriptionContent.text = it.description
        }
    }
}