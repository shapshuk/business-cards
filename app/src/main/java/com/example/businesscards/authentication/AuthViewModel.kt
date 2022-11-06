package com.example.businesscards.authentication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.businesscards.User
import com.google.firebase.auth.AuthCredential


class AuthViewModel(application: Application?) : AndroidViewModel(
    application!!
) {

    private val authRepository: AuthRepository = AuthRepository()
    var _authenticatedUser = MutableLiveData<User?>()
    var authenticatedUser: LiveData<User?> = _authenticatedUser

    fun signInWithGoogle(googleAuthCredential: AuthCredential?) {
        authRepository.firebaseSignInWithGoogle(googleAuthCredential).observeForever { user ->
            _authenticatedUser.value = user
        }
    }
}