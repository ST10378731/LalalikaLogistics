package com.example.lalalikalogistics.models

data class User(
    val uid: String = "",
    val role: String = "user",  // "admin" or "user"
    val homeLat: Double = 0.0,
    val homeLng: Double = 0.0
)