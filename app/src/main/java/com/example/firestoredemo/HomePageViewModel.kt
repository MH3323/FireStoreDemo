package com.example.firestoredemo

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomePageState(
    val error: String = "",
    val jobList: List<Job> = emptyList()
)

class HomePageViewModel : ViewModel() {
    val state = mutableStateOf<HomePageState>(HomePageState())

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()


    private val _userImage = MutableStateFlow<String?>(null)
    val userImage: StateFlow<String?>
        get() = _userImage

    private val _userInformation = MutableStateFlow<User?>(null)
    val userInformation: StateFlow<User?>
        get() = _userInformation

    private val db = Firebase.firestore

    fun loadStuff() {
        viewModelScope.launch {
            _isLoading.value = true
            delay(500L)
            _isLoading.value = false

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
    }

    init {
         loadStuff()


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