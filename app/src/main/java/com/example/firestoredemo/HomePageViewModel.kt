package com.example.firestoredemo

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
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

    private val _cv = MutableStateFlow<Uri?>(null)

    val cv: StateFlow<Uri?>
        get() = _cv

    private val _userInformation = MutableStateFlow<User?>(null)
    val userInformation: StateFlow<User?>
        get() = _userInformation

    private val db = Firebase.firestore

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _jobs = MutableStateFlow(listOf<Job>())
    val jobs = searchText
        .debounce(1000L)
        .onEach {
            _isSearching.update { true }
        }
        .combine(_jobs) { text, jobs ->
        if (text.isBlank()) {
            jobs
        } else {
            delay(2000L)
            jobs.filter {
                it.doesMatchSearchQuery(text)
            }
        }
    }
        .onEach { _isSearching.update { false } }
        .stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        _jobs.value
    )

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

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

                    _jobs.value = it
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

    fun setCV(uri: Uri) {
        _cv.value = uri
    }

    fun uploadPDF() {
        uploadPDFToFirebase(
            cv.value,
            onSuccess = { it ->
                Log.d("PDF", "Here is the download link of pdf " + it)
            },
            onFailure = { error ->
                Log.d("PDF", error)
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