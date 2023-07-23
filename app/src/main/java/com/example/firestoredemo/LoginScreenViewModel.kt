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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class LoginScreenState(
    val email: String = "",
    val password: String = "",
    val error: String = "",
)

public class LoginScreenViewModel : ViewModel() {
    val auth = Firebase.auth
    val state = mutableStateOf(LoginScreenState())
    val db = Firebase.firestore

    fun setEmail(value: String) {
        state.value = state.value.copy(email = value) // use copy for just changing the email value, the remain properties still intact
    }

    fun setPassword(value: String) {
        state.value = state.value.copy(password = value)
    }

    fun verify(): Boolean {
        if (state.value.email.isNotEmpty() && state.value.password.isNotEmpty()) {
            return true
        }
        return false
    }

    fun setError(error: String) {
        state.value = state.value.copy(error = error)
    }

    fun login(
        goToHomePage: () -> Unit
    ) {
        auth.signInWithEmailAndPassword(state.value.email, state.value.password)
            .addOnCompleteListener { task ->
                if (auth.currentUser?.isEmailVerified() == true) {
                    if (task.isSuccessful) {
                        Log.d(TAG, "Successfully log in")
                        val temp = auth.currentUser
                        if (temp != null) {
                            val uid = temp.uid
                            db.collection("user").document(uid).get()
                                .addOnSuccessListener { documentSnapshot ->
                                    if (documentSnapshot.exists()) {
                                        // The document exists, you can access its data
                                        // Handle the data as needed
                                        val user = documentSnapshot.toObject(User::class.java)
                                    } else {
                                        // The document doesn't exist
                                        // Handle this case if needed
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    // Handle any errors that occurred while fetching the document
                                }
                            goToHomePage()
                        }
                    } else {
                        val exception = task.exception
                        Log.d(TAG, exception.toString())
                        setError(exception.toString())
                    }
                } else {
                    setError("Please confirm your email")
                }
            }
            .addOnFailureListener { e ->
                Log.d(TAG, e.toString())
                setError(e.toString())
            }
    }

}
