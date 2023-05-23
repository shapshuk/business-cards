package com.example.businesscards.ui.cardsmainfragment.contacts

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.businesscards.data.CardUiModel
import com.example.businesscards.util.getUid
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ContactsViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,

    ) : ViewModel() {

    val cardsItemFromServer = MutableLiveData<MutableList<CardUiModel>>()
    val personalCardsFromServer = MutableLiveData<MutableList<CardUiModel>>()
    val isDataFailed = MutableLiveData<Unit>()

    init {
        viewModelScope.launch {
            readFirebaseData()
        }
    }

    private suspend fun readFirebaseData() {
        withContext(viewModelScope.coroutineContext) {
            getUid(firebaseAuth).let {
                Firebase.database.getReference("Users").child(it).child("OtherCards").get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            cardsItemFromServer.postValue(task.result.children.mapNotNull { data ->
                                Log.d("RealtimeDatabase", data.toString())
                                data.getValue<CardUiModel>()
                            } as MutableList<CardUiModel>)
                        } else {
                            isDataFailed.postValue(Unit)
                        }
                    }
            }
        }
    }
}