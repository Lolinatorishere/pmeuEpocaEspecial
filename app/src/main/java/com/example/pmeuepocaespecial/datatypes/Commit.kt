package com.example.pmeuepocaespecial.datatypes

data class Commit(val id: Int,
                      val taskListId: Int,
                      val description: String,
                      val percentDone: Int)