package com.example.firestoredemo

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun SignUpScreen(
    navController: NavController
) {
    Column() {
        Button(
            onClick = {
                navController.navigate("jobFinderSignUpScreen")
            }
        ) {
            Text(text = "Job finder")
        }
        Button(
            onClick = {
                navController.navigate("employerSignUpScreen")
            }
        ) {
            Text(text = "Employer")
        }
    }
}