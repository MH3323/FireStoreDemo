package com.example.firestoredemo

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlin.math.sign

@Composable
fun App() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "loginScreen") {
        addLoginScreen(navController = navController)
        addSignUpScreen(navController = navController)
        addHomePageScreen(navController = navController)
        addLoginSuccessfullyScreen(navController = navController)
        addSignUpSuccessfullyScreen(
            navController = navController,
        )
    }
}

private fun NavGraphBuilder.addLoginScreen(navController: NavController) {
    composable("loginScreen") {
        val loginScreenViewModel = viewModel<LoginScreenViewModel>()
        LoginScreen(
            loginScreenViewModel = loginScreenViewModel,
            navController = navController
        )
    }
}

private fun NavGraphBuilder.addSignUpScreen(navController: NavController) {
    composable("signUpScreen") {
        val signUpViewModel = viewModel<SignUpViewModel>()
        SignUpScreen(
            signUpViewModel = signUpViewModel,
            navController = navController,
        )
    }
}

private fun NavGraphBuilder.addHomePageScreen(navController: NavController) {
    composable("homePageScreen") {
//        val homePageViewModel = viewModel<HomePageViewModel>()
        HomePageScreen()
    }
}

private fun NavGraphBuilder.addLoginSuccessfullyScreen(navController: NavController) {
    composable("loginSuccessfullyScreen") {
        LoginSuccessfullyScreen(navController = navController)
    }
}

private fun NavGraphBuilder.addSignUpSuccessfullyScreen(
    navController: NavController,
) {
    composable("signUpSuccessfullyScreen") {
        SignUpSuccessfullyScreen(navController = navController)
    }
}