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
import com.example.pmeuepocaespecial.activities.admin.ProjectManageTasksActivity
import com.example.pmeuepocaespecial.activities.user.ProjectTaskActivity
import com.example.pmeuepocaespecial.datatypes.Task
import com.example.pmeuepocaespecial.helpers.DatabaseHelper

class TaskProjectAdapter(
    private val taskList: List<Task>,
    private val context: Context,
    private val projectId: Int
) : RecyclerView.Adapter<TaskProjectAdapter.TaskProjectViewHolder>() {

    private val taskIntent = Intent(context, ProjectTaskActivity::class.java)
    private val dbHelper = DatabaseHelper(context)
    class TaskProjectViewHolder(projectView: View) : RecyclerView.ViewHolder(projectView) {

        var taskTitle: TextView = itemView.findViewById(R.id.taskName)
        var taskCategory: TextView = itemView.findViewById(R.id.taskCategory)
        var taskStatus: TextView = itemView.findViewById(R.id.taskStatus)
        var taskButton: Button = itemView.findViewById<Button>(R.id.taskButton)
        var taskDelete: Button = itemView.findViewById<Button>(R.id.taskDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskProjectViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.task_item_layout, parent, false)
        return TaskProjectViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskProjectViewHolder, position: Int) {
        val currentItem = taskList[position]
        holder.taskTitle.text = currentItem.title
        holder.taskCategory.text = currentItem.category
        if(currentItem.status == 0){
            holder.taskStatus.text = context.getString(R.string.status_active)
        }else{
            holder.taskStatus.text = context.getString(R.string.status_inactive)
        }
        holder.taskDelete.visibility = View.VISIBLE
        holder.taskButton.text = context.getString(R.string.select_string)
        holder.taskButton.setOnClickListener {
            taskIntent.putExtra("taskId", currentItem.id)
            taskIntent.putExtra("projectId", projectId)
            context.startActivity(taskIntent)
        }

        holder.taskDelete.setOnClickListener {
            val intent = Intent(context, ProjectManageTasksActivity::class.java)
            intent.putExtra("projectId", projectId)
            dbHelper.deleteTask(currentItem.id, context )
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = taskList.size
}