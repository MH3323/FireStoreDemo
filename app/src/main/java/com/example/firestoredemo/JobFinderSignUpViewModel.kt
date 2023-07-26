package com.example.firestoredemo

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class SignUpState(
    val fullname: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val passwordMatch: Boolean = true,
    val error: String = "",
    val img: Bitmap? = null,
    val successfullySignUp: Boolean = false,
    val imgUrl: String? = null
)


class JobFinderSignUpViewModel : ViewModel(){
    val auth = Firebase.auth
    val db = Firebase.firestore
    val state = mutableStateOf(SignUpState())
//    val selectedImage = mutableStateOf<Bitmap?>(null)

    private val _imgUrl = MutableStateFlow<String?>(null)

    val imgUrl: StateFlow<String?>
        get() = _imgUrl

    private val _imgUri = MutableStateFlow<Uri?>(null)
    val imgUri: StateFlow<Uri?>
        get() = _imgUri

    fun setEmail(value: String) {
        state.value = state.value.copy(email = value)
    }

    fun setPassword(value: String) {
        state.value = state.value.copy(password = value)
    }

    fun setConfirmPassword(value: String) {
        state.value = state.value.copy(confirmPassword = value)
    }

    fun confirmPassword(): Boolean {
        if (state.value.password != state.value.confirmPassword) {
            state.value = state.value.copy(error = "Password does not match")
            return false
        }
        return true
    }

    fun setFullName(value: String)
    {
        state.value = state.value.copy(fullname = value)
    }

    fun verify(): Boolean {
        return state.value.fullname.isNotEmpty() && state.value.email.isNotEmpty() && state.value.password.isNotEmpty() && state.value.confirmPassword.isNotEmpty()
    }

    fun setError(error: String) {
        state.value = state.value.copy(error = error)
    }

    fun signUp(
        goToHomePage: () -> Unit,
    ) {
        // create new account in authentication custom email/password
        auth.createUserWithEmailAndPassword(state.value.email, state.value.password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    val user: FirebaseUser? = auth.currentUser
                    if(user != null)
                    {
                        Log.d("Upload Image", "the imgUrl prepared to send to database: " + imgUrl.value.toString())

                        val newUser: User = User(
                            fullName = state.value.fullname,
                            email = state.value.email,
                            password = state.value.password,
                            role = "jobfinder",
                            imgUrl = imgUrl.value,
                        )
                        addNewUserInfoToFireStoreWithUID(user.uid, newUser)
                        auth.currentUser?.sendEmailVerification()
                            ?.addOnSuccessListener {
                                Log.d("Email verify", "Send email successfully")
                            }
                        Log.d(TAG, "Successfully create account")
                    }

                    goToHomePage()
                } else {
                    Log.d(TAG, task.exception.toString())
                    state.value = state.value.copy(error = task.exception.toString())
                }
            }
    }

    fun setImage(bitmap: Bitmap?, imgUri: Uri?) {
        state.value = state.value.copy(
            img = bitmap,
        )
        _imgUri.value = imgUri
    }

    fun setImgUrl(imgUrl: String?) {
        state.value = state.value.copy(
            imgUrl = imgUrl
        )
    }

    fun onSuccessUploadImage(Url: String?, goToHomePage: () -> Unit) {
        _imgUrl.value = Url
        Log.d("Upload Image", "add image to _imgUrl: " + imgUrl.value.toString())
        signUp {
            goToHomePage()
        }
    }

    fun onFailedUploadImage(
        msg: String,
        goToHomePage: () -> Unit
    ) {
        Log.w("Upload Image", msg)
        signUp {
            goToHomePage()
        }
    }
}