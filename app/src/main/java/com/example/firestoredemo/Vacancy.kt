package com.example.firestoredemo

data class Vacancy(
    val vid: String,
    val title: String,
    val minSalary: Int,
    val maxSalary: Int,
    val workLocation: String,
    val jobType: String,
    val updatedDate: String,
    val expiredDate: String,
    val isActive: Boolean,
)
