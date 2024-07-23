package com.example.pmeuepocaespecial.adapters

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pmeuepocaespecial.R
import com.example.pmeuepocaespecial.activities.user.ProjectUserActivity
import com.example.pmeuepocaespecial.datatypes.User

class UserProjectAdapter(
    private val userList: List<User>,
    private val context: Context,
    private val projectId: Int
) : RecyclerView.Adapter<UserProjectAdapter.UserProjectViewHolder>() {

    private val userIntent = Intent(context, ProjectUserActivity::class.java)
    class UserProjectViewHolder(projectView: View) : RecyclerView.ViewHolder(projectView) {

        var userProfile: ImageView = itemView.findViewById(R.id.userImage)
        var userName: TextView = itemView.findViewById(R.id.userUsername)
        var userEmail: TextView = itemView.findViewById(R.id.email)
        var userButton: Button = itemView.findViewById<Button>(R.id.userButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserProjectViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_item_layout, parent, false)
        return UserProjectViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserProjectViewHolder, position: Int) {
        val currentItem = userList[position]
        if(currentItem.pfp != null){
            val bitmap = BitmapFactory.decodeByteArray(currentItem.pfp, 0, currentItem.pfp.size)
            holder.userProfile.setImageBitmap(bitmap)
        }else{
            holder.userProfile.setImageResource(R.mipmap.ic_launcher)
        }
        holder.userName.text = currentItem.username
        holder.userEmail.text = currentItem.email
        holder.userButton.setOnClickListener {
            userIntent.putExtra("userId", currentItem.id)
            userIntent.putExtra("projectId", projectId)
            context.startActivity(userIntent)
        }
    }

    override fun getItemCount() = userList.size
}