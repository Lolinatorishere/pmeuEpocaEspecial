package com.example.pmeuepocaespecial.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pmeuepocaespecial.R
import com.example.pmeuepocaespecial.activities.user.ProjectTaskActivity
import com.example.pmeuepocaespecial.datatypes.Task

class TaskAdapter(private val taskList: List<Task>, private val context: Context) : RecyclerView.Adapter<TaskAdapter.ProjectViewHolder>() {

    private val taskIntent = Intent(context, ProjectTaskActivity::class.java)
    class ProjectViewHolder(projectView: View) : RecyclerView.ViewHolder(projectView) {
        var taskName: TextView = itemView.findViewById(R.id.taskName)
        var taskCategory: TextView = itemView.findViewById(R.id.taskCategory)
        var taskButton: Button = itemView.findViewById<Button>(R.id.taskButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.task_item_layout, parent, false)
        return ProjectViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val currentItem = taskList[position]
        holder.taskName.text = currentItem.title
        holder.taskCategory.text = currentItem.category
        holder.taskButton.setOnClickListener {
            taskIntent.putExtra("taskId", currentItem.id)
            context.startActivity(taskIntent)
        }
    }

    override fun getItemCount() = taskList.size
}