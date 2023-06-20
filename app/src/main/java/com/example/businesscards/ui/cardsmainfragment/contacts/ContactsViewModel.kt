package com.example.businesscards.ui.cardsmainfragment.contacts

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.businesscards.data.CardUiModel
import com.example.businesscards.util.getUid
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
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
//    val personalCardsFromServer = MutableLiveData<MutableList<CardUiModel>>()
    val isDataFailed = MutableLiveData<Unit>()

    init {
        viewModelScope.launch {
            readFirebaseData()
        }
    }

//    private suspend fun readFirebaseData() {
//        withContext(viewModelScope.coroutineContext) {
//            getUid(firebaseAuth).let { userId ->
//                Firebase.database.getReference("Users").child(userId).child("OtherCards").get()
//                    .addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//                            cardsItemFromServer.postValue(task.result.children.mapNotNull { data ->
//                                Log.d("RealtimeDatabase", data.toString())
//                                data.getValue<CardUiModel>()
//                            } as MutableList<CardUiModel>)
//                        } else {
//                            isDataFailed.postValue(Unit)
//                        }
//                    }
//            }
//        }
//    }

    private suspend fun readFirebaseData() {
        withContext(viewModelScope.coroutineContext) {
            getUid(firebaseAuth).let { userId ->
                Firebase.database.getReference("Users").child(userId).child("OtherCards").get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val cardIds = task.result.value as? List<String>
                            if (cardIds != null) {
                                val personalCards = mutableListOf<CardUiModel>()
                                val cardsReference = Firebase.database.getReference("Cards")
                                val fetchCardsTasks = mutableListOf<Task<DataSnapshot>>()

                                for (cardId in cardIds) {
                                    val fetchCardTask = cardsReference.child(cardId).get()
                                    fetchCardsTasks.add(fetchCardTask)
                                }

                                Tasks.whenAllComplete(fetchCardsTasks)
                                    .addOnSuccessListener { taskList ->
                                        for (fetchCardTask in fetchCardsTasks) {
                                            if (fetchCardTask.isSuccessful) {
                                                val cardSnapshot = fetchCardTask.result
                                                val cardUiModel = cardSnapshot.getValue(CardUiModel::class.java)
                                                if (cardUiModel != null) {
                                                    personalCards.add(cardUiModel)
                                                }
                                            }
                                        }
                                        Log.d("Contacts", personalCards.toString())
                                        cardsItemFromServer.postValue(personalCards)
                                    }
                                    .addOnFailureListener {
                                        isDataFailed.postValue(Unit)
                                    }
                            } else {
                                cardsItemFromServer.postValue(mutableListOf())
                            }
                        } else {
                            isDataFailed.postValue(Unit)
                        }
                    }
            }
        }
    }
}