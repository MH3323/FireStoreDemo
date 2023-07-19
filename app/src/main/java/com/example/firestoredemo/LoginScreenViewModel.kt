package com.example.firestoredemo

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class User(val email: String, val password: String)

public class LoginScreenViewModel : ViewModel() {
    val auth = Firebase.auth
    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val showError = mutableStateOf(false)
    val errorMessage = mutableStateOf("")

    fun setEmail(value: String) {
        email.value = value
    }

    fun setPassword(value: String) {
        password.value = value
    }

    fun login() {
        auth.signInWithEmailAndPassword(email.value, password.value)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Successfully log in")
                } else {
                    val exception = task.exception
                    if (exception is FirebaseAuthException) {
                        showError.value = true
                        errorMessage.value = exception.localizedMessage ?: "Unknown error occurred"
                    }
                    Log.d(TAG, exception.toString())
                }
            }
    }

}
