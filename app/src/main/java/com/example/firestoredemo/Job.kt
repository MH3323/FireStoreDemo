package com.example.firestoredemo

import com.google.firebase.firestore.DocumentSnapshot

class Job (
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val companyId: String = "",
    val companyName: String = "",
) {
    companion object {
        fun fromFirestore(document: DocumentSnapshot): Job {
            return Job(
                id = document.id,
                name = document["name"] as String,
                description = document["description"] as String,
                companyName = document["companyName"] as String,
            )
        }
    }

    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            "$name",
            "$description",
            "$companyName"
        )

        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }
}


