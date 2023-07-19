package com.example.firestoredemo

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.firestoredemo.ui.theme.FireStoreDemoTheme
import kotlin.math.sign

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    signUpViewModel: SignUpViewModel
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
         Text(
                text = "Sign Up",
                fontSize = 20.sp,
                modifier = Modifier
            )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        OutlinedTextField(
            value = signUpViewModel.state.value.fullname,
            onValueChange = { signUpViewModel.setFullName(it) },
            placeholder = {
                Text(
                    text = "Full Name"
                )
            }
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        OutlinedTextField(
            value = signUpViewModel.state.value.email,
            onValueChange = { signUpViewModel.setEmail(it) },
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
            value = signUpViewModel.state.value.password,
            onValueChange = { signUpViewModel.setPassword(it) },
            placeholder = {
                Text(
                    text = "Password"
                )
            }
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        OutlinedTextField(
            value = signUpViewModel.state.value.confirmPassword,
            onValueChange = { signUpViewModel.setConfirmPassword(it) },
            placeholder = {
                Text(
                    text = "Confirm password"
                )
            }
        )

        if (!signUpViewModel.state.value.passwordMatchError) {
            Spacer(
              modifier = Modifier.height(16.dp)
            )
            Text(
                text = "Password does not match",
                color = MaterialTheme.colorScheme.error,
            )
        }

        if (signUpViewModel.state.value.errorMessage != "") {
            Spacer(
              modifier = Modifier.height(16.dp)
            )

            Text(
                text = signUpViewModel.state.value.errorMessage,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Button(onClick = {
            signUpViewModel.confirmPassword(signUpViewModel.state.value.confirmPassword)
            if (signUpViewModel.state.value.passwordMatchError) {
                signUpViewModel.signUp {
                    navController.navigate("homePageScreen")
                }
            }
        }) {
            Text(
                text = "Sign up"
            )
        }

        TextButton(onClick = {
            navController.popBackStack()
        }) {
            Text(
                text = "Back to login"
            )
        }

    }
}

@Preview(showSystemUi = true)
@Composable
fun SignUpScreenPreview() {
    FireStoreDemoTheme {
    }
}