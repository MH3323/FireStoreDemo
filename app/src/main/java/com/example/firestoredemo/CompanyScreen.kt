package com.example.firestoredemo

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState

@Composable
fun CompanyScreen(
    companyId: String,
    companyViewModel: CompanyViewModel = CompanyViewModel()
) {
    val companyInformation by companyViewModel.companyInformation.collectAsState()

    if (companyInformation != null) {
        Text(
            text = companyInformation!!.name
        )
    } else {
        Text(
            text = "Empty"
        )
    }
}