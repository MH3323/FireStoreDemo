@file:Suppress("DEPRECATION")

package com.example.firestoredemo

import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.firestoredemo.ui.theme.FireStoreDemoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobFinderSignUpScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    jobFinderSignUpViewModel: JobFinderSignUpViewModel = viewModel<JobFinderSignUpViewModel>(),
) {
    val state = jobFinderSignUpViewModel.state.value

    // get local image
    val context = LocalContext.current
    val pickImage = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            jobFinderSignUpViewModel.setImage(bitmap = bitmap, imgUri = uri)
        }
    }

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

        state.img?.let {
            Image(
                bitmap = state.img.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )
        }

        OutlinedTextField(
            value = state.fullname,
            onValueChange = { jobFinderSignUpViewModel.setFullName(it) },
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
            value = state.email,
            onValueChange = { jobFinderSignUpViewModel.setEmail(it) },
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
            value = state.password,
            onValueChange = { jobFinderSignUpViewModel.setPassword(it) },
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
            value = state.confirmPassword,
            onValueChange = { jobFinderSignUpViewModel.setConfirmPassword(it) },
            placeholder = {
                Text(
                    text = "Confirm password"
                )
            }
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Button(
            onClick = {
//                selectImage()
                pickImage.launch("image/*")
            }
        ) {
            Text(
                text = "Upload your image"
            )
        }

        if (state.error != "") {
            Spacer(
              modifier = Modifier.height(16.dp)
            )

            Text(
                text = state.error,
                color = MaterialTheme.colorScheme.error
            )
        }



        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Button(onClick = {
            if (jobFinderSignUpViewModel.verify()) {
                if (jobFinderSignUpViewModel.confirmPassword()) {
                            uploadImageToFirebase(
                                contentResolver = context.contentResolver,
                                imageUri = jobFinderSignUpViewModel.imgUri.value,
                                onSuccess = {
                                    jobFinderSignUpViewModel.onSuccessUploadImage(
                                        Url = it,
                                        goToHomePage = {
                                            navController.navigate("signUpSuccessfullyScreen")
                                        }
                                    )
                                },
                                onFailure = {
                                    jobFinderSignUpViewModel.onFailedUploadImage(
                                        it,
                                        goToHomePage = {
                                                navController.navigate("signUpSuccessfullyScreen")
                                        }
                                    )
                                }
                            )
                }
            } else {
                jobFinderSignUpViewModel.setError("Some of the fields has not been filled yet")
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