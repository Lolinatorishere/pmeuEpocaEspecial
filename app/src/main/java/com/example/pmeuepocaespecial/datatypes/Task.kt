package com.example.pmeuepocaespecial.datatypes

data class Task(val id: Int,
                val title: String,
                val description: String,
                val dateCreated: String,
                val dateFinished: String,
                val percentDone: Int,
                val status: Int,
                val category: String,
                val projectId: Int)

data class TaskList(val id: Int,
                    val taskId: Int,
                    val userId: Int)

