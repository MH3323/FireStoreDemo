package com.example.firestoredemo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.firestoredemo.ui.theme.FireStoreDemoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    loginScreenViewModel: LoginScreenViewModel
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Login",
            fontSize = 20.sp,
            modifier = Modifier
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        OutlinedTextField(
            value = loginScreenViewModel.state.value.email,
            onValueChange = { loginScreenViewModel.setEmail(it) },
            placeholder = {
                Text(
                    text = "Email"
                )
            }
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        OutlinedTextField(
            value = loginScreenViewModel.state.value.password,
            onValueChange = { loginScreenViewModel.setPassword(it) },
            placeholder = {
                Text(
                    text = "Password"
                )
            }
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Button(onClick = {
            loginScreenViewModel.login {
                navController.navigate("homePageScreen")
            }
        }) {
            Text(
                text = "Login"
            )
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        TextButton(
            onClick = {
                navController.navigate("signUpScreen")
            }
        ) {
            Text(
                text = "Sign up"
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun MainScreenPreview() {

    FireStoreDemoTheme {
    }
}