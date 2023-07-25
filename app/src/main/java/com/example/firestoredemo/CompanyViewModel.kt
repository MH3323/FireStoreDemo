package com.example.firestoredemo

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CompanyViewModel() : ViewModel() {
    private val _companyInformation = MutableStateFlow<Company?>(null)

    val companyInformation: StateFlow<Company?>
        get() = _companyInformation

    fun getCompanyInformation(companyId: String) {
        getCompanyInformationFromFirebase(
            companyId,
            onSuccess = {
                _companyInformation.value = it
            },
            onFailure = {
                Log.d("Company", it)
            }
        )
    }
}