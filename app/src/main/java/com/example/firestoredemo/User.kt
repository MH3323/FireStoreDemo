package com.example.firestoredemo

import android.graphics.Bitmap

data class User(
    val fullName:String = "",
    val email: String = "",
    val password: String = "",
    val role: String = "",
    val imgUrl: String? = null,
)
