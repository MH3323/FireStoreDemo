package com.example.firestoredemo

import android.nfc.Tag
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class HomePageState(
    val error: String = "",
)

class HomePageViewModel : ViewModel() {
    val state = mutableStateOf(HomePageState())

    private val _userInformation = MutableStateFlow<User?>(null)
    val userInformation: StateFlow<User?>
        get() = _userInformation

    private val db = Firebase.firestore

    fun getUserInformation(uid: String) {
        getUserInformationFromFireStore(
            onSuccess = { userInformation ->
                _userInformation.value = userInformation
            },
            onFailure = {
                state.value = state.value.copy(error = it)
            },
            uid = uid
        )
    }



//    init{
////
//        val user = auth.currentUser
//        if (user != null) {
//            val userRef = db.collection("users").document(user.uid)
//            userRef.get()
//                .addOnSuccessListener { userSnapShot ->
//                    if (userSnapShot.exists()) {
//                        state.value = state.value.copy(user = User(
//                            fullName = userSnapShot.getString("fullName") ?: "",
//                        ))
//                        Log.d("homepage", "success display")
//                    }
//                }
//                .addOnFailureListener { e ->
//                    state.value = state.value.copy(error = e.toString())
//                    Log.d("homepage", e.toString())
//                }
//        } else {
//            state.value = state.value.copy(error = "User not found")
//            Log.d("homepage", "User not found")
//        }
//    }
}