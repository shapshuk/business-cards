package com.example.businesscards.splashscreen

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.businesscards.User
import com.google.firebase.auth.FirebaseUser


class SplashViewModel : ViewModel() {

    private var splashRepository: SplashRepository = SplashRepository()
    var isUserAuthenticatedLiveData: LiveData<User>? = null
//    var userLiveData: LiveData<User>? = splashRepository.addUserYoLiveData(uid)


    fun checkIfUserIsAuthenticated() {
        isUserAuthenticatedLiveData = splashRepository.checkIfUserIsAuthenticatedInFirebase()
    }
}