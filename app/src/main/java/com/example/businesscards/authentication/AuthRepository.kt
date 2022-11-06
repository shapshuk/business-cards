package com.example.businesscards.authentication

import android.content.Intent
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.businesscards.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth


class AuthRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()

    fun firebaseSignInWithGoogle(googleAuthCredential: AuthCredential?): MutableLiveData<User?> {
        val authenticatedUserMutableLiveData = MutableLiveData<User?>()
        firebaseAuth.signInWithCredential(googleAuthCredential!!)
            .addOnCompleteListener { authTask: Task<AuthResult> ->
                if (authTask.isSuccessful) {
                    val isNewUser =
                        authTask.result.additionalUserInfo!!.isNewUser
                    val firebaseUser = firebaseAuth.currentUser
                    if (firebaseUser != null) {
                        val uid = firebaseUser.uid
                        val name = firebaseUser.displayName
                        val email = firebaseUser.email
                        val user = User(uid, name, email)
                        user.isNew = isNewUser
                        user.isAuthenticated = true
                        authenticatedUserMutableLiveData.value = user
                    }
                } else {
                    Log.d("Auth", authTask.exception!!.message!!)
                    authenticatedUserMutableLiveData.value = null
                }
            }
        return authenticatedUserMutableLiveData
    }
}