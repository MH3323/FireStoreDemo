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
    val passwordMatch: Boolean = true,
    val error: String = "",
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

    fun confirmPassword(
        goToHomePage: () -> Unit
    ) {
        if (state.value.password != state.value.confirmPassword) {
            state.value = state.value.copy(error = "Password does not match")
        } else {
            state.value = state.value.copy(error = "")
            signUp {
                goToHomePage()
            }
        }
    }

    fun setFullName(value: String)
    {
        state.value = state.value.copy(fullname = value)
    }

    fun verify(): Boolean {
        return state.value.fullname.isNotEmpty() && state.value.email.isNotEmpty() && state.value.password.isNotEmpty() && state.value.confirmPassword.isNotEmpty()
    }

    fun setError(error: String) {
        state.value = state.value.copy(error = error)
    }

    fun signUp(
        goToHomePage: () -> Unit,
    ) {
        // create new account in authentication custom email/password
        auth.createUserWithEmailAndPassword(state.value.email, state.value.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Successfully create account")
                    val user: FirebaseUser? = auth.currentUser
                    if(user != null)
                    {
//                        val temp = hashMapOf(
//                            "fullName" to state.value.fullname,
//                            "email" to state.value.email,
//                            "password" to state.value.password,
//                            "role" to "jobfinder"
//                        )
                        val newUser: User = User(
                            fullName = state.value.fullname,
                            email = state.value.email,
                            password = state.value.password,
                            role = "jobfinder"
                        )
                        addNewUserInfoToFireStoreWithUID(user.uid, newUser)
                    }

                    goToHomePage()
                } else {
                    Log.d(TAG, task.exception.toString())
                    state.value = state.value.copy(error = task.exception.toString())
                }
            }
    }
}