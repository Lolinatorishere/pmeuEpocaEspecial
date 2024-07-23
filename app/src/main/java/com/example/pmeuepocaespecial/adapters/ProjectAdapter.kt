package com.example.pmeuepocaespecial.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.pmeuepocaespecial.R
import com.example.pmeuepocaespecial.activities.user.ProjectActivity
import com.example.pmeuepocaespecial.datatypes.Project

class ProjectAdapter(private val projectList: List<Project>, private val context: Context) : RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>() {

    class ProjectViewHolder(projectView: View) : RecyclerView.ViewHolder(projectView) {
        var projectImage: ImageView = itemView.findViewById(R.id.projectImage)
        var projectName: TextView = itemView.findViewById(R.id.projectName)
        var projectCategory: TextView = itemView.findViewById(R.id.projectCategory)
        var projectButton: Button = itemView.findViewById<Button>(R.id.projectButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.project_item_layout, parent, false)
        return ProjectViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val currentItem = projectList[position]
        holder.projectImage.setImageResource(R.mipmap.ic_launcher)
        holder.projectName.text = currentItem.title
        holder.projectCategory.text = currentItem.category
        holder.projectButton.setOnClickListener {
            val projectIntent = Intent(context , ProjectActivity::class.java)
            projectIntent.putExtra("projectId", currentItem.id)
            context.startActivity(projectIntent)
        }
    }

    override fun getItemCount() = projectList.size
}
