package com.example.businesscards.ui.signin

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.businesscards.User
import com.example.businesscards.core.CARDS
import com.example.businesscards.data.CardUiModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SignInViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val databaseReference: DatabaseReference
) : ViewModel() {

    private val isAuthSuccessful = MutableLiveData<Boolean>()

    val listOfDataFromFirebase = MutableLiveData<List<CardUiModel>>()
    val isNavigateToBottomSheetNeeded = MutableLiveData<Unit>()
    val isDataFailed = MutableLiveData<Unit>()


    private val firebaseDataObserver = Observer<Boolean> { isAuthSuccessful ->
        if (isAuthSuccessful) {
            viewModelScope.launch { readFirebaseData() }
        } else {
            isNavigateToBottomSheetNeeded.value = Unit
        }
    }

    init {
        isAuthSuccessful.observeForever(firebaseDataObserver)
        Log.d("SignInViewModel", firebaseAuth.currentUser?.uid.toString())
    }

    override fun onCleared() {
        super.onCleared()
        isAuthSuccessful.removeObserver(firebaseDataObserver)
    }

    private suspend fun readFirebaseData() {
        withContext(viewModelScope.coroutineContext) {
            firebaseAuth.uid?.let {
                databaseReference.child(it).child(CARDS).get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            listOfDataFromFirebase.postValue(task.result.children.mapNotNull { data ->
                                data.getValue<CardUiModel>()
                            })
                        } else {
                            isDataFailed.postValue(Unit)
                        }
                    }
            }
        }
    }

    fun firebaseAuthWithGoogle(idToken: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
                isAuthSuccessful.postValue(it.isSuccessful)
            }
        }
    }
}