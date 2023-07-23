package com.example.firestoredemo

import android.content.ContentResolver
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.InputStream


fun addNewUserInfoToFireStoreWithUID(uid: String, newUser: User) {
    val db = Firebase.firestore

    db.collection("users").document(uid)
        .set(newUser)
        .addOnSuccessListener { documentReference ->
            Log.d(TAG, "Add successfully data for user ${uid}")  }
        .addOnFailureListener{ e ->
            Log.d(TAG, "Error")
        }
}

fun signInWithFirebaseAuthentication(
    onSuccess: () -> Unit,
    onFailure: (String) -> Unit,
    email: String,
    password: String
) {
    val auth = Firebase.auth
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "Successfully log in")
                onSuccess()
            }
            else {
                val exception = task.exception
                Log.d(TAG, exception.toString())
                onFailure(exception.toString())
            }
        }
        .addOnFailureListener { e ->
            onFailure(e.toString())
        }
}

fun getUserInformationFromFireStore(
    uid: String,
    onSuccess: (User?) -> Unit,
    onFailure: (String) -> Unit,
) {
    val userDocumentPath = "users/$uid"
    val db = Firebase.firestore

    db.document(userDocumentPath).get()
        .addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val userInformation = documentSnapshot.toObject(User::class.java)
                onSuccess(userInformation)
            } else {
                onFailure("Document does not exist")
            }
        }
        .addOnFailureListener {
            onFailure(it.toString())
        }
}

fun uploadImageToFirebase(
    contentResolver: ContentResolver,
    imageUri: Uri?,
    onSuccess: (String?) -> Unit,
    onFailure: (String) -> Unit
) {
        if (imageUri != null) {
            val inputStream: InputStream? = contentResolver.openInputStream(imageUri)
            if (inputStream != null) {
                val imageBytes = inputStream.readBytes()
                val fileName = "image_${System.currentTimeMillis()}.jpg"

                val storageReference = Firebase.storage.reference.child("images/$fileName")
                val uploadTask = storageReference.putBytes(imageBytes)
                    .addOnSuccessListener {
                        storageReference.downloadUrl.addOnSuccessListener {
                            val downloadUrl = it.toString()
                            Log.d("upload image", "successfully upload image to firebase: " + downloadUrl)
                            onSuccess(downloadUrl)
                        }

                    }
                    .addOnFailureListener { e ->
                        onFailure(e.toString())
                    }
            } else {
                onFailure("inputStream is null")
            }
        } else {
            onFailure("ImgURI is null")
        }
}

fun getImageFromFirebase(
    imageUrl: String,
    onImageFetched: (Bitmap) -> Unit
) {
        GlobalScope.launch(Dispatchers.IO) {
            Log.d("userimage", "in getImageFromFirebase: " + imageUrl)
            val storageReference = Firebase.storage.reference.child(imageUrl)
            val maxImageSize: Long = 1024 * 1024 * 5 // max 5MB
            storageReference.getBytes(maxImageSize)
                .addOnSuccessListener {
                    val byteArray = it
                    val bitmap = byteArrayToBitmap(byteArray)
                    onImageFetched(bitmap)
                    Log.d("userimage", "successfully")
                }
                .addOnFailureListener { e ->
                    Log.d("userimage", e.toString())
                }
        }
}

fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
}




