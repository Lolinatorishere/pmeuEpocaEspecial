package com.example.pmeuepocaespecial.helpers

import android.content.Context
import com.example.pmeuepocaespecial.datatypes.User

class UserAuthHelper(private val context: Context){
    fun login(username: String, password: String): Boolean {
        val user = checkCredentials(username, password) ?: return false
        setUserInfo(true, user.id)
        return true;
    }

    fun logout(){
        setUserInfo(false, 0)
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

    fun checkProjectAuth(projectId: Int): Int{
        //checks if admin if yes auto returns true
        if(checkUserAuth()) return 2; //returns admin code
        val dbHelper = DatabaseHelper(context)
        val sharedLogin = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val userId = sharedLogin.getInt("userId", 0)
        val userPerm = dbHelper.getProjectPermissions(projectId, userId)
        if(userPerm) return 1
        return 0;

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