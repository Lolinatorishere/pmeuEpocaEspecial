package com.example.pmeuepocaespecial.datatypes
data class Project(val id: Int,
                   val title: String,
                   val description: String,
                   val date: String,
                   val status: String,
                   val category: String,
                   val image: String,
                   val members: List<Int>)
