package com.example.businesscards.ui.splashscreen

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.businesscards.core.CARDS
import com.example.businesscards.data.CardUiModel
import com.example.businesscards.util.getUid
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SplashScreenViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val auth: FirebaseAuth,
    private val databaseReference: DatabaseReference
) : ViewModel() {

    private val isUserRegistered = MutableLiveData<Boolean>()

    val listOfDataFromFirebase = MutableLiveData<MutableList<CardUiModel>>()

    val isNavigateToViewPagerNeeded = MutableLiveData<Unit>()

    val isDataFailed = MutableLiveData<Unit>()

    private val firebaseDataObserver = Observer<Boolean> { isUserRegistered ->
        if (isUserRegistered) {
            Log.d("SplashScreenViewModel", "User is registered")
            viewModelScope.launch { readFirebaseData() }
        } else {
            Log.d("SplashScreenViewModel", "User is not registrered")
            isNavigateToViewPagerNeeded.value = Unit
        }
    }

    init {
        isUserRegistered.observeForever(firebaseDataObserver)
    }

    fun splashDelay() = viewModelScope.launch {
        delay(1000L)
        isUserRegistered.postValue(auth.currentUser != null)
    }

    private suspend fun readFirebaseData() {
        withContext(viewModelScope.coroutineContext) {
            getUid(firebaseAuth).let {
                Firebase.database.getReference("Cards").get()
                    .addOnCompleteListener { task ->
                        Log.d("RealtimeDatabase", task.toString())
                        if (task.isSuccessful) {
                            listOfDataFromFirebase.postValue(task.result.children.mapNotNull { data ->
                                data.getValue<CardUiModel>()
                            } as MutableList<CardUiModel>)
                        } else {
                            isDataFailed.postValue(Unit)
                        }
                    }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        isUserRegistered.removeObserver(firebaseDataObserver)
    }

}