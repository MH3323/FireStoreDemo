package com.example.firestoredemo

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource

@Composable
fun JobDetailScreen(
    jobId: String,
    jobDetailViewModel: JobDetailViewModel = JobDetailViewModel(jobId)
) {
    val state = jobDetailViewModel.job.collectAsState()

    Column() {
            Image(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = "Company image"
            )
            Column() {
                Text(
                    text = "Job name: " + state.value?.name ?: ""
                )
                Text(
                    text = "Job Description: " + state.value?.description ?: ""
                )
            }
        Column() {
            Button(onClick = { /*TODO*/ }) {
                Text(
                    text = "Apply"
                )
            }
            Button(onClick = { /*TODO*/ }) {
                Row() {
                    Text(
                        text = "Save"
                    )
                    Icon(
                        imageVector = Icons.Outlined.Favorite,
                        contentDescription = "Heart Icon"
                    )
                }
            }
        }
    }
}