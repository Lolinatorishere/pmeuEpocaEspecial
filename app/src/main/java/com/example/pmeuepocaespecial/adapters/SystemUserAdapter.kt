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
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.pmeuepocaespecial.R
import com.example.pmeuepocaespecial.activities.admin.ManageUsersActivity
import com.example.pmeuepocaespecial.activities.admin.UserEditActivity
import com.example.pmeuepocaespecial.datatypes.User
import com.example.pmeuepocaespecial.helpers.DatabaseHelper
import com.example.pmeuepocaespecial.helpers.UserAuthHelper

class SystemUserAdapter(private val userList: List<User>, private val context: Context) : RecyclerView.Adapter<SystemUserAdapter.SystemUserViewHolder>() {

    private val userIntent = Intent(context, UserEditActivity::class.java)
    private val userAuth = UserAuthHelper(context)
    private val dbHelper = DatabaseHelper(context)

    class SystemUserViewHolder(projectView: View) : RecyclerView.ViewHolder(projectView) {

        var userProfile: ImageView = itemView.findViewById(R.id.userImage)
        var userName: TextView = itemView.findViewById(R.id.userUsername)
        var userEmail: TextView = itemView.findViewById(R.id.email)
        var userButton: Button = itemView.findViewById<Button>(R.id.userButton)
        var userDelete: Button = itemView.findViewById<Button>(R.id.userDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SystemUserViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_item_layout, parent, false)
        return SystemUserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SystemUserViewHolder, position: Int) {
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
            val intent = Intent(context, UserEditActivity::class.java)
            intent.putExtra("userId", currentItem.id)
            context.startActivity(intent)
        }
        if(userAuth.checkUserAuth()){
            if (!userAuth.checkUserId(currentItem.id)){
                holder.userDelete.visibility = View.VISIBLE
            }
        }else{
            holder.userDelete.visibility = View.GONE
        }
        holder.userDelete.setOnClickListener {
            val message = dbHelper.deleteUser(currentItem.id, context)
            if(message.returnBoolean){
                Toast.makeText(context, message.returnString, Toast.LENGTH_SHORT).show()
                val intent = Intent(context, ManageUsersActivity::class.java)
                context.startActivity(intent)
            }else{
                Toast.makeText(context, message.returnString, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount() = userList.size
}
