package com.example.firestoredemo

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun HomePageScreen(
    homePageViewModel: HomePageViewModel = viewModel<HomePageViewModel>()
) {
    val state = homePageViewModel.state.value
    val currentUser = Firebase.auth.currentUser

    Column {
        Text(
            text = "Homepage"
        )

        if (currentUser != null) {
            homePageViewModel.getUserInformation(currentUser.uid)
            val userInformation by homePageViewModel.userInformation.collectAsState()

            Text(
                text = userInformation.toString()
            )
        } else {
            Text(
                text = state.error
            )
        }
    }

}