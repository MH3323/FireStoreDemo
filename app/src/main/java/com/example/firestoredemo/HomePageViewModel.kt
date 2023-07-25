package com.example.firestoredemo

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class HomePageState(
    val error: String = "",
    val jobList: List<Job> = emptyList()
)

class HomePageViewModel : ViewModel() {
    val state = mutableStateOf<HomePageState>(HomePageState())

    private val _userImage = MutableStateFlow<String?>(null)
    val userImage: StateFlow<String?>
        get() = _userImage

    private val _userInformation = MutableStateFlow<User?>(null)
    val userInformation: StateFlow<User?>
        get() = _userInformation

    private val db = Firebase.firestore

    init {
        getJobListFromFirebase(
            onSuccess = {
                state.value = state.value.copy(
                    jobList = it
                )
            },
            onFailure = {
                Log.d("Job List", it)
            }
        )
    }

    fun getUserInformation(uid: String) {
        getUserInformationFromFireStore(
            onSuccess = { userInformation ->
                _userInformation.value = userInformation

                _userInformation.value?.imgUrl?.let { it ->
                    Log.d("userimage", it)
                    _userImage.value = it
//                    getImageFromFirebase(
//                        it,
//                        onImageFetched = { image ->
//                            _userImage.value = image
//                        }
//                    )

                }

            },
            onFailure = {
                state.value = state.value.copy(error = it)
            },
            uid = uid
        )
    }


}