package com.example.pmeuepocaespecial.helpers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.Settings.Global.getString
import com.example.pmeuepocaespecial.R
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
                    "title VARCHAR(255) NOT NULL UNIQUE," +
                    "description TEXT," +
                    "date_created DATETIME DEFAULT now()," +
                    "date_finished DATETIME DEFAULT null" +
                    "points INTEGER NOT NULL DEFAULT 0," +
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
                    "project_id INTEGER NOT NULL," +
                    "user_permissions TINYINT(1) NOT NULL," +
                    "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE," +
                    "FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE," +
                    "UNIQUE(user_id, project_id)" +
                    "); " +

                    "CREATE TABLE IF NOT EXISTS task_list (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "task_id INTEGER NOT NULL," +
                    "user_id INTEGER ," +
                    "FOREIGNKEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE," +
                    "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE," +
                    "UNIQUE(task_id, user_id) " +
                    "); " +

                    "CREATE TABLE IF NOT EXISTS commits (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "task_list_id INTEGER NOT NULL," +
                    "date DATETIME DEFAULT now()," +
                    "description TEXT," +
                    "point_value INTEGER NOT NULL," +
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
        val values = ContentValues().apply {
            put("name", user.name)
            put("username", user.username)
            put("pfp", user.pfp)
            put("email", user.email)
            put("password", user.password)
            put("user_permission", user.userPermission)
        }
        db.insert("users", null, values)
        db.close()
    }

    fun insertTask(task: Task) {
        val db = writableDatabase
        val values = ContentValues().apply {
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
        val values = ContentValues().apply {
            put("title", project.title)
            put("description", project.description)
            put("date_created", project.date)
            put("status", project.status)
            put("category", project.category)
        }
        db.insert("projects", null, values)
        db.close()
    }

    ////------------------------------------------ READ --------------------------------------------////
    fun checkUserCredentials(username: String, password: String): User? {
        val db = readableDatabase
        val query = "SELECT * FROM users WHERE username = $username"
        val cursor = db.rawQuery(query, null)
        val empty_user = null
        if (cursor.moveToFirst()) {
            val db_user = User(
                cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                cursor.getString(cursor.getColumnIndexOrThrow("name")),
                cursor.getString(cursor.getColumnIndexOrThrow("username")),
                cursor.getBlob(cursor.getColumnIndexOrThrow("pfp")),
                cursor.getString(cursor.getColumnIndexOrThrow("email")),
                cursor.getString(cursor.getColumnIndexOrThrow("password")),
                cursor.getInt(cursor.getColumnIndexOrThrow("user_permission"))
            )
            cursor.close()
            db.close()
            if (password != db_user.password) {
                return empty_user
            }
            return getUserPublic(db_user.id)
        }
        db.close()
        return empty_user
    }

    private fun checkUsernameUniqueness(username: String): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM users WHERE username = $username"
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) return false
        db.close()
        return true //username is unique
    }

    private fun checkTitleUniqueness(title: String): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM projects WHERE title = $title"
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) return false
        db.close()
        return true //username is unique
    }

    fun getUserPublic(id: Int): User? {
        val db = readableDatabase
        val query = "SELECT * FROM users WHERE id = $id"
        val cursor = db.rawQuery(query, null)
        val empty_user = null
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

    fun getUser(id: Int): User? {
        val db = readableDatabase
        val query = "SELECT * FROM users WHERE id = $id"
        val cursor = db.rawQuery(query, null)
        val empty_user = null
        if (cursor.moveToFirst()) {
            val db_user = User(
                cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                cursor.getString(cursor.getColumnIndexOrThrow("name")),
                cursor.getString(cursor.getColumnIndexOrThrow("username")),
                cursor.getBlob(cursor.getColumnIndexOrThrow("pfp")),
                cursor.getString(cursor.getColumnIndexOrThrow("email")),
                cursor.getString(cursor.getColumnIndexOrThrow("password")),
                cursor.getInt(cursor.getColumnIndexOrThrow("user_permission"))
            )
            cursor.close()
            db.close()
            return db_user
        }
        db.close()
        return empty_user
    }

    fun getProject(id: Int): Project? {
        val db = readableDatabase
        val query = "SELECT * FROM projects WHERE id = $id"
        val cursor = db.rawQuery(query, null)
        val emptyProject = null
        if (cursor.moveToFirst()) {
            val dbProject = Project(
                cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                cursor.getString(cursor.getColumnIndexOrThrow("title")),
                cursor.getString(cursor.getColumnIndexOrThrow("description")),
                cursor.getString(cursor.getColumnIndexOrThrow("date")),
                cursor.getInt(cursor.getColumnIndexOrThrow("status")),
                cursor.getString(cursor.getColumnIndexOrThrow("category")),
            )
            cursor.close()
            db.close()
            return dbProject
        }
        db.close()
        return emptyProject
    }

    fun getTask(id: Int): Task? {
        val db = readableDatabase
        val query = "SELECT * FROM tasks WHERE id = $id"
        val cursor = db.rawQuery(query, null)
        val empty_task = null
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

    fun getCommit(id: Int): Commit? {
        val db = readableDatabase
        val query = "SELECT * FROM task_commits WHERE id = $id"
        val cursor = db.rawQuery(query, null)
        val empty_commit = null
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

    fun getAllUsers(): List<User> {
        val db = readableDatabase
        var db_users = mutableListOf<User>()
        val query = "SELECT * FROM users"
        val cursor = db.rawQuery(query, null)
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

    fun getProjectUsers(project_id: Int): List<User> {
        val db = readableDatabase
        val proj_users = mutableListOf<User>()
        val query = "SELECT * FROM member_list WHERE project_id = $project_id"
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            getUserPublic(cursor.getInt(cursor.getColumnIndexOrThrow("user_id")))?.let {
                proj_users.add(
                    it
                )
            }
        }
        cursor.close()
        db.close()
        return proj_users
    }

    // if true user is editor if not user is a normal user or an admin if they are an admin
    // this function will be ignored
    fun getProjectPermissions(projectId: Int, userId: Int): Boolean {
        val db = readableDatabase
        var userPermissions: Boolean = false
        val query = "SELECT * FROM member_list WHERE project_id = $projectId AND user_id = $userId"
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToFirst()) {
            val permission = cursor.getInt(cursor.getColumnIndexOrThrow("user_permissions"))
            if (permission == 0) {
                userPermissions = false
            } else {
                userPermissions = true
            }
        }
        cursor.close()
        db.close()
        return userPermissions
    }

    fun getTaskUsers(task_id: Int): List<User> {
        val db = readableDatabase
        val task_users = mutableListOf<User>()
        val query = "SELECT * FROM task_list WHERE task_id = $task_id"
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            getUserPublic(cursor.getInt(cursor.getColumnIndexOrThrow("user_id")))?.let {
                task_users.add(
                    it
                )
            }
        }
        cursor.close()
        db.close()
        return task_users
    }

    fun getProjectTasks(project_id: Int): List<Task> {
        val db = readableDatabase
        val proj_tasks = mutableListOf<Task>()
        val query = "SELECT * FROM tasks WHERE project_id = $project_id"
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val proj_task = Task(
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
            proj_tasks.add(proj_task)
        }
        cursor.close()
        db.close()
        return proj_tasks
    }

    fun getCommits(tasklist_id: Int): List<Commit> {
        val db = readableDatabase
        val task_commits = mutableListOf<Commit>()
        val query = "SELECT * FROM task_list WHERE task_list_id = $tasklist_id"
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            getCommit(cursor.getInt(cursor.getColumnIndexOrThrow("task_id")))?.let {
                task_commits.add(
                    it
                )
            }
        }
        cursor.close()
        db.close()
        return task_commits
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
            while (tasklist_cursor.moveToNext()) {
                val user_id =
                    tasklist_cursor.getInt(tasklist_cursor.getColumnIndexOrThrow("user_id"))
                val tasklist_id =
                    tasklist_cursor.getInt(tasklist_cursor.getColumnIndexOrThrow("id"))
                val user = getUserPublic(user_id)
                if (user != null) {
                    task_users.add(user)
                }
                task_commits.addAll(getCommits(tasklist_id))
            }
            val task = getTask(task_id)
            val fullTaskData = task?.let {
                FullTaskData(
                    task_users = task_users,
                    task_details = it,
                    task_commits = task_commits
                )
            }
            if (fullTaskData != null) {
                proj_commits.add(fullTaskData)
            }
        }
        cursor.close()
        db.close()
        return proj_commits
    }

    fun getUserCommitsFromTask(user_id: Int, task_id: Int): List<Commit> {
        val db = readableDatabase
        val user_commits = mutableListOf<Commit>()
        val query = "SELECT * FROM task_list WHERE user_id = $user_id AND task_id = $task_id"
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val taskList_id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val commits = getCommits(taskList_id)
            user_commits.addAll(commits)
        }
        cursor.close()
        db.close()
        return user_commits
    }

    fun getUserCommitsFromProject(user_id: Int, project_id: Int): List<Commit> {
        val db = readableDatabase
        val user_commits = mutableListOf<Commit>()
        val query = "SELECT * FROM tasks WHERE project_id = $project_id"
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val task_id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val commits = getUserCommitsFromTask(user_id, task_id)
            user_commits.addAll(commits)
        }
        cursor.close()
        db.close()
        return user_commits
    }

    ////----------------------------------------- UPDATE -------------------------------------------////

    fun updateUser(user: User, userId: Int, context: Context): Any {
        val db = readableDatabase
        val originalUser = getUser(userId)
        var name: String? = null
        var username: String? = null
        var pfp: ByteArray? = null
        var email: String? = null
        var password: String? = null
        var permission: Int? = null
        if (originalUser != null) {
            name = if (user.name.trim() != originalUser.name.trim() && user.name.isNotEmpty()) {
                user.name.trim()
            } else {
                originalUser.name
            }

            username =
                if (user.username.trim() != originalUser.username.trim() && user.username.isNotEmpty()) {
                    if (!checkUsernameUniqueness(user.username)) {
                        return context.getString(R.string.user_update_error_username_not_unique)
                    }
                    user.username.trim()
                } else {
                    originalUser.username
                }

            pfp = if (!user.pfp.contentEquals(originalUser.pfp) && user.pfp.isNotEmpty()) {
                user.pfp
            } else {
                originalUser.pfp
            }

            email = if (user.email.trim() != originalUser.email.trim() && user.email.isNotEmpty()) {
                user.email.trim()
            } else {
                originalUser.email
            }

            password =
                if (user.password.trim() != originalUser.password.trim() && user.password.isNotEmpty()) {
                    user.password.trim()
                } else {
                    originalUser.password
                }

            if (user.userPermission != originalUser.userPermission) {
                permission = if (user.userPermission in 1 downTo 0) {
                    user.userPermission
                } else {
                    originalUser.userPermission
                }
            }

        } else {
            return context.getString(R.string.user_update_user_not_exists);
        }

        val values = ContentValues().apply {
            put("name", name)
            put("username", username)
            put("pfp", pfp)
            put("email", email)
            put("password", password)
            put("permission", permission)
        }

        val success = db.update("users", values, "id = ?", arrayOf(userId.toString()))
        db.close()
        return if (success > 0) {
            context.getString(R.string.user_update_success_start) +
                    username +
                    context.getString(R.string.user_update_success_end)
        } else {
            context.getString(R.string.user_update_error_update_failure)
        }
    }

    fun updateProject(project: Project, projectId: Int, context: Context): String {
        val db = readableDatabase
        val originalProject = getProject(projectId)
        var title: String? = null
        var description: String? = null
        var status: Int? = null
        var category: String? = null
        if (originalProject != null) {

            title =
                if (project.title.trim() != originalProject.title.trim() && project.title.isNotEmpty()) {
                    if (!checkTitleUniqueness(project.title)) {
                        return context.getString(R.string.project_update_error_title_not_unique)
                    }
                    project.title.trim()
                } else {
                    originalProject.title
                }

            description =
                if (project.description.trim() != originalProject.description.trim() && project.description.isNotEmpty()) {
                    project.description.trim()
                } else {
                    originalProject.description
                }

            if (project.status != originalProject.status) {
                status = if (project.status in 1 downTo 0) {
                    project.status
                } else {
                    originalProject.status
                }
            }

            category =
                if (project.category != originalProject.category && project.category.isNotEmpty()) {
                    project.category.trim()
                } else {
                    originalProject.category
                }

        } else {
            return context.getString(R.string.project_update_project_not_exists);
        }

        val values = ContentValues().apply {
            put("title", title)
            put("description", description)
            put("status", status)
            put("category", category)
        }

        val success = db.update("projects", values, "id = ?", arrayOf(projectId.toString()))
        db.close()
        return if (success > 0) {
            context.getString(R.string.project_update_success_start) +
                    title +
                    context.getString(R.string.project_update_success_end)
        } else {
            context.getString(R.string.project_update_error_update_failure)
        }
    }

    fun updateTask(task: Task, taskId: Int, context: Context): String {
        val db = readableDatabase
        val originalTask = getTask(taskId)
        var title: String? = null
        var description: String? = null
        var category: String? = null

        if (originalTask != null) {
            title = if (task.title.trim() != task.title.trim() && task.title.isNotEmpty()) {
                task.title.trim()
            } else {
                originalTask.title
            }

            description =
                if (task.description.trim() != originalTask.description.trim() && task.description.isNotEmpty()) {
                    task.description.trim()
                } else {
                    task.description
                }


            category = if (task.category != originalTask.category && task.category.isNotEmpty()) {
                task.category.trim()
            } else {
                originalTask.category
            }

        } else {
            return context.getString(R.string.task_update_task_not_exists);
        }

        val values = ContentValues().apply {
            put("title", title)
            put("description", description)
            put("category", category)
        }

        val success = db.update("tasks", values, "id = ?", arrayOf(taskId.toString()))
        db.close()
        return if (success > 0) {
            context.getString(R.string.task_update_success_start) +
                    title +
                    context.getString(R.string.task_update_success_end)
        } else {
            context.getString(R.string.task_update_error_update_failure)
        }
    }

    fun updateCommit(commit: Commit, commitId: Int, context: Context): String {
        val db = readableDatabase
        val originalCommit = getCommit(commitId)
        var description: String? = null
        var points: Int? = null

        if (originalCommit != null) {
            description =
                if (commit.description.trim() != originalCommit.description.trim() && commit.description.isNotEmpty()) {
                    commit.description.trim()
                } else {
                    commit.description
                }
            points = if (commit.points > 0) {
                commit.points
            } else {
                originalCommit.points
            }

        } else {
            return context.getString(R.string.commit_update_task_not_exists);
        }

        val values = ContentValues().apply {
            put("description", description)
            put("points", points)
        }

        val success = db.update("commits", values, "id = ?", arrayOf(commitId.toString()))
        db.close()
        return if (success > 0) {
            context.getString(R.string.commit_update_success_start) +
                    originalCommit.id +
                    context.getString(R.string.commit_update_success_end)
        } else {
            context.getString(R.string.commit_update_error_update_failure)
        }
    }

////----------------------------------------- DELETE -------------------------------------------////

    fun deleteUser(userId: Int): Int {
        val db = writableDatabase
        val whereArgs = arrayOf(userId.toString())
        val deletedRows = db.delete("users", "id = ?", whereArgs)
        db.close()
        return deletedRows // Number of rows deleted
    }

    fun deleteProject(projectId: Int): Int {
        val db = writableDatabase
        val whereArgs = arrayOf(projectId.toString())
        val deletedRows = db.delete("projects", "id = ?", whereArgs)
        db.close()
        return deletedRows // Number of rows deleted
    }

    fun deleteTask(taskId: Int): Int {
        val db = writableDatabase
        val whereArgs = arrayOf(taskId.toString())
        val deletedRows = db.delete("tasks", "id = ?", whereArgs)
        db.close()
        return deletedRows // Number of rows deleted
    }

    fun deleteCommit(commitId: Int): Int {
        val db = writableDatabase
        val whereArgs = arrayOf(commitId.toString())
        val deletedRows = db.delete("commits", "id = ?", whereArgs)
        db.close()
        return deletedRows // Number of rows deleted
    }

    fun removeUserFromProject(userId: Int, projectId: Int): Int{
        val db = writableDatabase
        val whereArgs = arrayOf(userId.toString(), projectId.toString())
        val deletedRows = db.delete("member_list", "user_id = ? AND project_id = ? ", whereArgs)
        db.close()
        return deletedRows // Number of rows deleted
    }

    fun removeUserFromTask(userId: Int, taskId: Int): Int{
        val db = writableDatabase
        val whereArgs = arrayOf(userId.toString(), taskId.toString())
        val deletedRows = db.delete("task_list", "user_id = ? AND task_id = ? ", whereArgs)
        db.close()
        return deletedRows // Number of rows deleted
    }

}
