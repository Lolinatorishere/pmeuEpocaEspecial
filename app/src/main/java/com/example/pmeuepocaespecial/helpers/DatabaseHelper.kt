package com.example.pmeuepocaespecial.helpers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.pmeuepocaespecial.datatypes.Commit
import com.example.pmeuepocaespecial.datatypes.FullTaskData
import com.example.pmeuepocaespecial.datatypes.Project
import com.example.pmeuepocaespecial.datatypes.Task
import com.example.pmeuepocaespecial.datatypes.User

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "pmeuproject.db" , null, 0) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createDatabase =
                "CREATE TABLE IF NOT EXISTS users (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT," +
                        "username VARCHAR(255) NOT NULL UNIQUE," +
                        "pfp LONGBLOB DEFAULT NULL," +
                        "email VARCHAR(255) NOT NULL UNIQUE," +
                        "password VARCHAR(255) NOT NULL," +
                        "user_permission TINYINT(1) NOT NULL" + //is admin or not
                "); " +

                "CREATE TABLE IF NOT EXISTS tasks (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "title VARCHAR(255) NOT NULL," +
                        "description TEXT," +
                        "date_created DATETIME DEFAULT now()," +
                        "date_finished DATETIME DEFAULT null" +
                        "percent_done INTEGER NOT NULL DEFAULT 0," +
                        "DATETIME DEFAULT null" +
                        "status TINYINT(1) NOT NULL," +
                        "category TEXT NOT NULL," +
                        "project_id INTEGER NOT NULL" +
                        "FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE," +
                "); " +

                "CREATE TABLE IF NOT EXISTS projects (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "title VARCHAR(255) UNIQUE," +
                        "description TEXT," +
                        "date_created DATETIME DEFAULT now()," +
                        "status TINYINT(1) NOT NULL," + // is manager or not
                        "category TEXT" +
                "); " +

                "CREATE TABLE IF NOT EXISTS member_list (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "user_id INTEGER NOT NULL," +
                        "user_id INTEGER NOT NULL," +
                        "user_permissions TINYINT(1) NOT NULL," +
                        "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE," +
                        "FOREIGN KEY (group_id) REFERENCES member_list(id) ON DELETE CASCADE," +
                "); " +

                "CREATE TABLE IF NOT EXISTS task_list (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "task_id INTEGER NOT NULL," +
                        "user_id INTEGER ," +
                        "FOREIGNKEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE," +
                        "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE," +
                        "UNIQUE(task_id, user_id) "+
                "); " +

                "CREATE TABLE IF NOT EXISTS commits (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "task_list_id INTEGER NOT NULL," +
                        "date DATETIME DEFAULT now()," +
                        "description TEXT," +
                        "percentage_done INTEGER NOT NULL," +
                        "FOREIGN KEY (task_list_id) REFERENCES task_list(id) ON DELETE CASCADE" +
                "); "

        db?.execSQL(createDatabase)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropDatabase = "DROP TABLE IF EXISTS users; " +
                "DROP TABLE IF EXISTS tasks; " +
                "DROP TABLE IF EXISTS projects; " +
                "DROP TABLE IF EXISTS member_list; " +
                "DROP TABLE IF EXISTS task_list; " +
                "DROP TABLE IF EXISTS task_commits; "
        db?.execSQL(dropDatabase)
        onCreate(db)
    }

////---------------------------------------- CREATE --------------------------------------------////

    fun insertUser(user: User) {
        val db = writableDatabase
        val values = ContentValues().apply{
            put("name", user.name)
            put("username", user.username)
            put("pfp", user.pfp)
            put("email", user.email)
            put("password", user.password)
            put("user_permission", user.userPermission)
        }
        db.insert("users" , null , values)
        db.close()
    }
    fun insertTask(task: Task) {
        val db = writableDatabase
        val values = ContentValues().apply{
            put("title", task.title)
            put("description", task.description)
            put("date_created", task.dateCreated)
            put("date_finished", task.dateFinished)
            put("percent_done", task.percentDone)
            put("status", task.status)
            put("category", task.category)
            put("project_id", task.projectId)
        }
        db.close()
    }

    fun insertProject(project: Project) {
        val db = writableDatabase
        val values = ContentValues().apply{
            put("title", project.title)
            put("description", project.description)
            put("date_created", project.date)
            put("status", project.status)
            put("category", project.category)
        }
        db.insert("projects" , null , values)
        db.close()
    }

////------------------------------------------ READ --------------------------------------------////
    fun getUserPublic(id: Int): User? {
        val db = readableDatabase
        val query = "SELECT * FROM users WHERE id = $id"
        val cursor = db.rawQuery(query, null);
        val empty_user = null;
        if (cursor.moveToFirst()) {
            val db_user = User(
                cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                cursor.getString(cursor.getColumnIndexOrThrow("name")),
                cursor.getString(cursor.getColumnIndexOrThrow("username")),
                cursor.getBlob(cursor.getColumnIndexOrThrow("pfp")),
                cursor.getString(cursor.getColumnIndexOrThrow("email")),
                password = "",
                cursor.getInt(cursor.getColumnIndexOrThrow("user_permission"))
            )
            cursor.close()
            db.close()
            return db_user
        }
        db.close()
        return empty_user
    }

    fun getTask(id: Int): Task? {
        val db = readableDatabase
        val query = "SELECT * FROM tasks WHERE id = $id"
        val cursor = db.rawQuery(query, null);
        val empty_task = null;
        if (cursor.moveToFirst()) {
            val db_task = Task(
                cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                cursor.getString(cursor.getColumnIndexOrThrow("title")),
                cursor.getString(cursor.getColumnIndexOrThrow("description")),
                cursor.getString(cursor.getColumnIndexOrThrow("dateCreated")),
                cursor.getString(cursor.getColumnIndexOrThrow("dateFinished")),
                cursor.getInt(cursor.getColumnIndexOrThrow("percent_done")),
                cursor.getInt(cursor.getColumnIndexOrThrow("status")),
                cursor.getString(cursor.getColumnIndexOrThrow("category")),
                cursor.getInt(cursor.getColumnIndexOrThrow("project_id")),

            )
            cursor.close()
            db.close()
            return db_task
        }
        db.close()
        return empty_task
    }

    fun getCommit(id: Int):  Commit?{
        val db = readableDatabase
        val query = "SELECT * FROM task_commits WHERE id = $id"
        val cursor = db.rawQuery(query, null);
        val empty_commit = null;
        if (cursor.moveToFirst()) {
            val db_commit = Commit(
                cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                cursor.getInt(cursor.getColumnIndexOrThrow("taskListId")),
                cursor.getString(cursor.getColumnIndexOrThrow("description")),
                cursor.getInt(cursor.getColumnIndexOrThrow("percentDone")),
            )
            cursor.close()
            db.close()
            return db_commit
        }
        db.close()
        return empty_commit
    }

    fun getAllUsers(): List<User>{
        val db = readableDatabase
        var db_users = mutableListOf<User>()
        val query = "SELECT * FROM users"
        val cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            val db_user = User(
                cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                cursor.getString(cursor.getColumnIndexOrThrow("name")),
                cursor.getString(cursor.getColumnIndexOrThrow("username")),
                cursor.getBlob(cursor.getColumnIndexOrThrow("pfp")),
                cursor.getString(cursor.getColumnIndexOrThrow("email")),
                password = "",
                cursor.getInt(cursor.getColumnIndexOrThrow("user_permission"))
            )
            db_users.add(db_user)
        }
        cursor.close()
        db.close()
        return db_users
    }

    fun getProjectUsers(project_id: Int): List<User>{
        val db = readableDatabase
        val proj_users = mutableListOf<User>()
        val query = "SELECT * FROM member_list WHERE project_id = $project_id"
        val cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            getUserPublic(cursor.getInt(cursor.getColumnIndexOrThrow("user_id")))?.let { proj_users.add(it) }
        }
        cursor.close()
        db.close()
        return proj_users;
    }

    fun getTaskUsers(task_id: Int): List<User>{
        val db = readableDatabase
        val task_users = mutableListOf<User>()
        val query = "SELECT * FROM task_list WHERE task_id = $task_id"
        val cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            getUserPublic(cursor.getInt(cursor.getColumnIndexOrThrow("user_id")))?.let { task_users.add(it) }
        }
        cursor.close()
        db.close()
        return task_users;
    }

    fun getProjectTasks(project_id: Int): List<Task>{
        val db = readableDatabase
        val proj_tasks = mutableListOf<Task>()
        val query = "SELECT * FROM tasks WHERE project_id = $project_id"
        val cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            val proj_task = Task (
                cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                cursor.getString(cursor.getColumnIndexOrThrow("string")),
                cursor.getString(cursor.getColumnIndexOrThrow("description")),
                cursor.getString(cursor.getColumnIndexOrThrow("date_created")),
                cursor.getString(cursor.getColumnIndexOrThrow("date_finished")),
                cursor.getInt(cursor.getColumnIndexOrThrow("percent_done")),
                cursor.getInt(cursor.getColumnIndexOrThrow("status")),
                cursor.getString(cursor.getColumnIndexOrThrow("category")),
                cursor.getInt(cursor.getColumnIndexOrThrow("project_id"))
            )
            proj_tasks.add(proj_task);
        }
        cursor.close()
        db.close()
        return proj_tasks;
    }

    fun getCommits(tasklist_id: Int): List<Commit>{
        val db = readableDatabase
        val task_commits = mutableListOf<Commit>()
        val query = "SELECT * FROM task_list WHERE task_list_id = $tasklist_id"
        val cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            getCommit(cursor.getInt(cursor.getColumnIndexOrThrow("task_id")))?.let { task_commits.add(it) }
        }
        cursor.close()
        db.close()
        return task_commits;
    }

    fun getAllTaskDataFromProject(project_id: Int): List<FullTaskData> {
        val db = readableDatabase
        val proj_commits = mutableListOf<FullTaskData>()
        val query = "SELECT * FROM tasks WHERE project_id = $project_id"
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val task_id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val tasklist_query = "SELECT * FROM task_list WHERE task_id = $task_id"
            val tasklist_cursor = db.rawQuery(tasklist_query, null)
            val task_commits = kotlin.collections.mutableListOf<Commit>()
            val task_users = mutableListOf<User>()
            while(tasklist_cursor.moveToNext()){
                val user_id = tasklist_cursor.getInt(tasklist_cursor.getColumnIndexOrThrow("user_id"))
                val tasklist_id = tasklist_cursor.getInt(tasklist_cursor.getColumnIndexOrThrow("id"))
                val user = getUserPublic(user_id)
                if (user != null) {
                    task_users.add(user)
                }
                task_commits.addAll(getCommits(tasklist_id));
            }
            val task = getTask(task_id);
            val fullTaskData = task?.let {
                FullTaskData(
                    task_users = task_users,
                    task_details = it,
                    task_commits = task_commits
                )
            }
            if (fullTaskData != null) {
                proj_commits.add(fullTaskData)
            };
        }
        cursor.close()
        db.close()
        return proj_commits;
    }

    fun getUserCommitsFromTask(user_id: Int, task_id: Int): List<Commit>{
        val db = readableDatabase
        val user_commits = mutableListOf<Commit>()
        val query = "SELECT * FROM task_list WHERE user_id = $user_id AND task_id = $task_id"
        val cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            val taskList_id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val commits = getCommits(taskList_id);
            user_commits.addAll(commits);
        }
        cursor.close()
        db.close()
        return user_commits;
    }

    fun getUserCommitsFromProject(user_id: Int, project_id: Int): List<Commit>{
        val db = readableDatabase
        val user_commits = mutableListOf<Commit>()
        val query = "SELECT * FROM tasks WHERE project_id = $project_id"
        val cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            val task_id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val commits = getUserCommitsFromTask(user_id, task_id)
            user_commits.addAll(commits)
        }
        cursor.close()
        db.close()
        return user_commits;
    }
}