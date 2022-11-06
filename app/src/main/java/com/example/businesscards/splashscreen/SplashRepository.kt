package com.example.businesscards.splashscreen

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.businesscards.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore


class SplashRepository {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private var user : User = User(null, null, null)
//    private val rootRef: FirebaseFirestore = FirebaseFirestore.getInstance()
//    private val usersRef: CollectionReference = rootRef.collection(USERS)

    fun checkIfUserIsAuthenticatedInFirebase(): MutableLiveData<User> {
        val isUserAuthenticateInFirebaseMutableLiveData = MutableLiveData<User>()
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null) {
            user.isAuthenticated = false
            isUserAuthenticateInFirebaseMutableLiveData.setValue(user)
        } else {
            user.uid = firebaseUser.uid
            user.isAuthenticated = true
            isUserAuthenticateInFirebaseMutableLiveData.setValue(user)
        }
        return isUserAuthenticateInFirebaseMutableLiveData
    }

//    fun addUserToLiveData(uid: String?): MutableLiveData<User>? {
//        val userMutableLiveData = MutableLiveData<User>()
//        usersRef.document(uid).get().addOnCompleteListener { userTask ->
//            if (userTask.isSuccessful()) {
//                val document: DocumentSnapshot = userTask.getResult()
//                if (document.exists()) {
//                    val user: User =
//                        document.toObject(User::class.java)
//                    userMutableLiveData.setValue(user)
//                }
//            } else {
//                Log.d("Splash Repository", userTask.getException().getMessage())
//            }
//        }
//        return userMutableLiveData
//    }
}