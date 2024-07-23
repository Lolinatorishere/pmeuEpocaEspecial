package com.example.pmeuepocaespecial.helpers

import android.content.Context
import android.content.Intent
import com.example.pmeuepocaespecial.datatypes.User

class UserAuthHelper(private val context: Context){
    fun login(username: String, password: String): Boolean {
        val user = checkCredentials(username, password) ?: return false
        setUserInfo(true, user.id)
        return true;
    }

    fun logout(){
        setUserInfo(false, 0)
        val logoutIntent = Intent("com.example.ACTION_LOGOUT")
        context.sendBroadcast(logoutIntent)
    }

    fun checkLoginStatus(): Boolean {
        val sharedLogin = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val dbHelper = DatabaseHelper(context)
        val user = dbHelper.getUser(sharedLogin.getInt("userId", 0))
        return sharedLogin.getBoolean("isLoggedIn", false)
    }

    fun checkUserAuth(): Boolean{
        val dbHelper = DatabaseHelper(context)
        val sharedLogin = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val userId = sharedLogin.getInt("userId", 0)
        val userInfo = dbHelper.getUserPublic(userId) ?: return false
        return userInfo.userPermission == 1
    }

    fun checkProjectAuth(projectId: Int): Boolean{
        //checks if admin if yes auto returns true
        val dbHelper = DatabaseHelper(context)
        val sharedLogin = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val userId = sharedLogin.getInt("userId", 0)
        val userPerm = dbHelper.getProjectPermissions(projectId, userId)
        if(userPerm) return true
        return false;

    }

    private fun setUserInfo(isLoggedIn: Boolean, user_id: Int) {
        val sharedLogin = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val loginEditor = sharedLogin.edit()
        loginEditor.putBoolean("isLoggedIn", isLoggedIn)
        loginEditor.putInt("userId", user_id)
        loginEditor.apply()
    }

    private fun checkCredentials(username: String, password: String): User? {
        val dbHelper = DatabaseHelper(context)
        return dbHelper.checkUserCredentials(username.trim(), password.trim())
    }

}