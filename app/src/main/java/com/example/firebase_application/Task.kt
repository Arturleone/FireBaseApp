package com.example.firebase_application

data class Task(
    var title: String = "",
    var description: String = "",
    var userId: String = "",
    var completed: Boolean = false,
    var id: String? = null
)
