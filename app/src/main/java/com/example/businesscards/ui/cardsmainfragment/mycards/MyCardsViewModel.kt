package com.example.businesscards.ui.cardsmainfragment.mycards

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.businesscards.data.CardUiModel
import com.example.businesscards.domain.CardsInteractor
import com.example.businesscards.util.getUid
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MyCardsViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val interactor: CardsInteractor,
) : ViewModel() {

    val personalCardsFromServer = MutableLiveData<MutableList<CardUiModel>>()
    val isDataFailed = MutableLiveData<Unit>()

    init {
        viewModelScope.launch {
            readFirebaseData()
        }
    }

    private suspend fun readFirebaseData() {
        withContext(viewModelScope.coroutineContext) {
            getUid(firebaseAuth).let { userId ->
                Firebase.database.getReference("Users").child(userId).child("PersonalCards").get()
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
                                        fetchCardsTasks.forEachIndexed { index, fetchCardTask ->
                                            if (fetchCardTask.isSuccessful) {
                                                val cardSnapshot = fetchCardTask.result
                                                val cardUiModel = cardSnapshot.getValue(CardUiModel::class.java)
                                                if (cardUiModel != null) {
                                                    personalCards.add(cardUiModel.copy(cardId = cardIds[index]))
                                                }
                                            }
                                        }

                                        Log.d("PersonalCards", personalCards.toString())
                                        personalCardsFromServer.postValue(personalCards)
                                    }
                                    .addOnFailureListener {
                                        isDataFailed.postValue(Unit)
                                    }
                            } else {
                                personalCardsFromServer.postValue(mutableListOf())
                            }
                        } else {
                            isDataFailed.postValue(Unit)
                        }
                    }
            }
        }
    }

    fun deleteCard(cardId: String) {
        viewModelScope.launch {
            getUid(firebaseAuth).let { userId ->

                Firebase.database.getReference("Cards").child(cardId).removeValue()

                val personalCardsRef = Firebase.database.getReference("Users").child(userId).child("PersonalCards")
                personalCardsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val currentCards = dataSnapshot.getValue<List<String>>()
                        val updatedCards = currentCards?.toMutableList() ?: mutableListOf()

                        // Find the index of the item to delete
                        val index = updatedCards.indexOf(cardId)
                        if (index != -1) {
                            updatedCards.removeAt(index)

                            // Update the list in the database
                            personalCardsRef.setValue(updatedCards)
                                .addOnSuccessListener {
                                    // The item has been deleted from the list
                                }
                                .addOnFailureListener { exception ->
                                    // An error occurred while deleting the item
                                }
                        } else {
                            // Handle the case where the item ID was not found in the list
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Error occurred while retrieving the current list of cards
                    }
                })
            }
            delay(500)
            readFirebaseData()
        }
    }
}