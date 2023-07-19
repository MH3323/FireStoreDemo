package com.example.firestoredemo

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

data class SignUpState(
    val fullname: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val passwordMatchError: Boolean = true,
    val errorMessage: String = "",
)

class SignUpViewModel : ViewModel(){
    val auth = Firebase.auth
    val state = mutableStateOf(SignUpState())
    val db = Firebase.firestore

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

    fun setFullName(value: String)
    {
        state.value = state.value.copy(fullname = value)
    }

    fun signUp(
        goToHomePage: () -> Unit,
    ) {
        auth.createUserWithEmailAndPassword(state.value.email, state.value.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Successfully create account")
                    val user: FirebaseUser? = auth.currentUser
                    if(user != null)
                    {
                        val temp = hashMapOf(
                            "fullName" to state.value.fullname,
                            "email" to state.value.email,
                            "password" to state.value.password,
                            "role" to "jobfinder"

                        )
                        db.collection("user").document(user.uid)
                            .set(temp)
                            .addOnSuccessListener { documentReference
                                -> Log.d(TAG, "Add successfully data for user ${user.uid}")  }
                            .addOnFailureListener{ e ->
                                Log.d(TAG, "Error")
                            }
                    }
                    goToHomePage()
                } else {
                    Log.d(TAG, task.exception.toString())
                    state.value = state.value.copy(errorMessage = task.exception.toString())
                }
            }
    }
}