package com.example.firestoredemo

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class JobDetailViewModel(
    private val jobId: String
) : ViewModel() {
    private val _job = MutableStateFlow<Job?>(null)

    val job: StateFlow<Job?>
        get() = _job

    init {
        getJobById(jobId)
    }

    private fun getJobById(id: String) {
        getJobByIdFromFirebase(
            id = id,
            onSuccess = {
                Log.d("Job Detail", "success get job data")
                Log.d("Job Detail", it.toString())
                _job.value = it
            },
            onFailure = {
                Log.d("Job Detail", it)
            }
        )
    }
}