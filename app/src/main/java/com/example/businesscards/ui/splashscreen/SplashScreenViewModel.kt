package com.example.businesscards.ui.splashscreen

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.businesscards.data.CardUiModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashScreenViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val auth: FirebaseAuth,
    private val databaseReference: DatabaseReference
) : ViewModel() {

    private val isUserRegistered = MutableLiveData<Boolean>()

    val listOfDataFromFirebase = MutableLiveData<MutableList<CardUiModel>>()

    val isNavigateToSignInNeeded = MutableLiveData<Unit>()
    val isNavigateToMainFragmentNeeded = MutableLiveData<Unit>()

    val isDataFailed = MutableLiveData<Unit>()

    private val firebaseDataObserver = Observer<Boolean> { isUserRegistered ->
        if (isUserRegistered) {
            Log.d("SplashScreenViewModel", "User is registered")
//            viewModelScope.launch { readFirebaseData() }
            isNavigateToMainFragmentNeeded.value = Unit
        } else {
            Log.d("SplashScreenViewModel", "User is not registrered")
            isNavigateToSignInNeeded.value = Unit
        }
    }

    init {
        isUserRegistered.observeForever(firebaseDataObserver)
    }

    fun splashDelay() = viewModelScope.launch {
        delay(1000L)
        isUserRegistered.postValue(auth.currentUser != null)
    }

//    private suspend fun readFirebaseData() {
//        withContext(viewModelScope.coroutineContext) {
//            getUid(firebaseAuth).let {
//                Log.d("User Id", it)
//                Firebase.database.getReference("Users").child(it).child("OtherCards").get()
//                    .addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//                            listOfDataFromFirebase.postValue(task.result.children.mapNotNull { data ->
//                                Log.d("Data from firebase", data.toString())
//                                data.getValue<CardUiModel>()
//                            } as MutableList<CardUiModel>)
//                        } else {
//                            Log.d("SplashScreenVM", "Task is not successful")
//                            isDataFailed.postValue(Unit)
//                        }
//                    }
//            }
//        }
//    }

    override fun onCleared() {
        super.onCleared()
        isUserRegistered.removeObserver(firebaseDataObserver)
    }

}