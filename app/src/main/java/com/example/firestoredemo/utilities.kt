package com.example.firestoredemo

import android.content.ContentResolver
import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
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

//fun signInWithFirebaseAuthentication(
//    onSuccess: () -> Unit,
//    onFailure: (String) -> Unit,
//    email: String,
//    password: String
//) {
//    val auth = Firebase.auth
//    auth.signInWithEmailAndPassword(email, password)
//        .addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                Log.d(TAG, "Successfully log in")
//                onSuccess()
//            }
//            else {
//                val exception = task.exception
//                Log.d(TAG, exception.toString())
//                onFailure(exception.toString())
//            }
//        }
//        .addOnFailureListener { e ->
//            onFailure(e.toString())
//        }
//}

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

//fun getImageFromFirebase(
//    imageUrl: String,
//    onImageFetched: (Bitmap) -> Unit
//) {
//        GlobalScope.launch(Dispatchers.IO) {
//            Log.d("userimage", "in getImageFromFirebase: " + imageUrl)
//            val storageReference = Firebase.storage.reference.child(imageUrl)
//            val maxImageSize: Long = 1024 * 1024 * 5 // max 5MB
//            storageReference.getBytes(maxImageSize)
//                .addOnSuccessListener {
//                    val byteArray = it
//                    val bitmap = byteArrayToBitmap(byteArray)
//                    onImageFetched(bitmap)
//                    Log.d("userimage", "successfully")
//                }
//                .addOnFailureListener { e ->
//                    Log.d("userimage", e.toString())
//                }
//        }
//}

//fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
//    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
//}

fun getCompanyInformationFromFirebase(
    companyId: String,
    onSuccess: (Company?) -> Unit,
    onFailure: (String) -> Unit,
) {
    val companyDocumentPath = "companies/$companyId"
    val db = Firebase.firestore

    db.document(companyDocumentPath).get()
        .addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val companyInformation = documentSnapshot.toObject(Company::class.java)
                onSuccess(companyInformation)
            } else {
                onFailure("Document does not exist")
            }
        }
        .addOnFailureListener {
            onFailure(it.toString())
        }
}

fun getJobListFromFirebase(
    onSuccess: (List<Job>) -> Unit,
    onFailure: (String) -> Unit
) {
    val companyDocumentPath = "jobs"
    val db = Firebase.firestore

    db.collection(companyDocumentPath).get()
        .addOnSuccessListener { snapshot ->
                val jobs = mutableListOf<Job>()

                for (document in snapshot.documents) {
                    jobs.add(Job.fromFirestore(document))
                }

                onSuccess(jobs)
        }
        .addOnFailureListener {
            onFailure(it.toString())
        }
}

fun getJobByIdFromFirebase(
    id: String,
    onSuccess: (Job?) -> Unit,
    onFailure: (String) -> Unit,
) {
    val jobDocumentPath = "jobs/" + id
    val db = Firebase.firestore

    db.document(jobDocumentPath).get()
        .addOnSuccessListener { snapshot ->
            val job = snapshot.toObject(Job::class.java)
            onSuccess(job)
        }
        .addOnFailureListener {
            onFailure(it.toString())
        }
}




