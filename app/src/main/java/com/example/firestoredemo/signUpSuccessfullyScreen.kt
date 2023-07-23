package com.example.firestoredemo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SignUpSuccessfullyScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = "Sign Up Successfully"
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                navController.navigate("loginScreen")
            }
        ) {
            Text(
                text = "Ok",
            )
        }
    }
}