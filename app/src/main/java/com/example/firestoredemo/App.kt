package com.example.firestoredemo

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlin.math.sign

@Composable
fun App() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "loginScreen") {
        addLoginScreen(navController = navController)
        addSignUpScreen(navController = navController)
        addHomePageScreen(navController = navController)
        addLoginSuccessfullyScreen(navController = navController)
        addSignUpSuccessfullyScreen(navController = navController)
        addJob(navController = navController)
        addJobFinderSignUpScreen(navController = navController)
        addEmployerSignUpScreen(navController = navController)
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
        SignUpScreen(
            navController = navController,
        )
    }
}

private fun NavGraphBuilder.addHomePageScreen(navController: NavController) {
    composable("homePageScreen") {
//        val homePageViewModel = viewModel<HomePageViewModel>()
        HomePageScreen(
            navController = navController
        )
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

private fun NavGraphBuilder.addCompanyPage(
    navController: NavController,
) {
    composable(
        route = "companyPage/{companyId}",
        arguments = listOf(navArgument("companyId") { type = NavType.StringType})
    ) { backStackEntry ->
        val companyId = backStackEntry.arguments?.getString("companyId") ?: ""
        CompanyScreen(companyId = companyId)
    }
}

private fun NavGraphBuilder.addJob(
    navController: NavController
) {
    composable(
        route = "jobs/{jobId}",
        arguments = listOf(navArgument("jobId") { type = NavType.StringType})
    ) { backStackEntry ->
        val jobId = backStackEntry.arguments?.getString("jobId") ?: ""
        JobDetailScreen(jobId = jobId)
    }
}

private fun NavGraphBuilder.addJobFinderSignUpScreen(
    navController: NavController
) {
    composable(
        route = "jobFinderSignUpScreen"
    ) {
        JobFinderSignUpScreen(
            navController = navController
        )
    }
}

private fun NavGraphBuilder.addEmployerSignUpScreen(
    navController: NavController
) {
    composable(
        route = "employerSignUpScreen"
    ) {
        EmployerSignUpScreen()
    }
}
