package com.example.pmeuepocaespecial.datatypes

data class FullTaskData(
    val task_users: List<User>,
    val task_details: Task,
    val task_commits: List<Commit>
)
