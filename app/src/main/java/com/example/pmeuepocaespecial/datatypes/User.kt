package com.example.pmeuepocaespecial.datatypes

data class User( val id: Int,
                 val name: String,
                 val username: String,
                 val pfp: ByteArray,
                 val email: String,
                 val password: String,
                 val userPermission: Int)
