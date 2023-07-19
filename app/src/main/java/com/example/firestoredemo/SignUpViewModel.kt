package com.example.firestoredemo

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

data class SignUpState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val passwordMatchError: Boolean = true,
    val errorMessage: String = "",
)

class SignUpViewModel : ViewModel(){
    val auth = Firebase.auth
    val state = mutableStateOf(SignUpState())

    fun setEmail(value: String) {
        state.value = state.value.copy(email = value)
    }

    fun setPassword(value: String) {
        state.value = state.value.copy(password = value)
    }

    fun setConfirmPassword(value: String) {
        state.value = state.value.copy(confirmPassword = value)
    }

    fun confirmPassword(value: String) {
        if (state.value.password != value) {
            state.value = state.value.copy(passwordMatchError = false)
        } else {
            state.value = state.value.copy(passwordMatchError = true)
        }
    }

    fun signUp(
        goToHomePage: () -> Unit,
    ) {
        auth.createUserWithEmailAndPassword(state.value.email, state.value.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Successfully create account")
                    goToHomePage()
                } else {
                    Log.d(TAG, task.exception.toString())
                    state.value = state.value.copy(errorMessage = task.exception.toString())
                }
            }
    }
}