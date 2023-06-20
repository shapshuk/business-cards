package com.example.businesscards.ui.cardsmainfragment.mycards

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.businesscards.core.utils.Resource
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

//    Log.d("PersonalCards", personalCards.toString())

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
                                        for (fetchCardTask in fetchCardsTasks) {
                                            if (fetchCardTask.isSuccessful) {
                                                val cardSnapshot = fetchCardTask.result
                                                val cardUiModel = cardSnapshot.getValue(CardUiModel::class.java)
                                                if (cardUiModel != null) {
                                                    personalCards.add(cardUiModel)
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


//
//    private suspend fun readFirebaseData() {
//        withContext(viewModelScope.coroutineContext) {
//            val userId = getUid(firebaseAuth)
//            if (userId != null) {
//                val database: FirebaseDatabase = FirebaseDatabase.getInstance()
//                val personalCardsRef: DatabaseReference =
//                    database.getReference("Users/$userId/PersonalCards")
//
//                personalCardsRef.addListenerForSingleValueEvent(object : ValueEventListener {
//                    override fun onDataChange(dataSnapshot: DataSnapshot) {
//                        val personalCards: MutableList<CardUiModel> = mutableListOf()
//
//                        for (cardSnapshot in dataSnapshot.children) {
//                            val cardId: String = cardSnapshot.getValue(String::class.java) ?: ""
//                            if (cardId.isNotEmpty()) {
//                                val cardRef: DatabaseReference =
//                                    database.getReference("Cards/$cardId")
//                                cardRef.addListenerForSingleValueEvent(object : ValueEventListener {
//                                    override fun onDataChange(cardDataSnapshot: DataSnapshot) {
//                                        val cardUiModel: CardUiModel? =
//                                            cardDataSnapshot.getValue(CardUiModel::class.java)
//                                        cardUiModel?.let {
//                                            personalCards.add(it)
//                                        }
//                                    }
//
//                                    override fun onCancelled(databaseError: DatabaseError) {
//                                        // Handle the error for individual card retrieval if needed
//                                    }
//                                })
//                            }
//                        }
//
//                        Log.d("PersonalCards", personalCardsRef.toString())
//                        Log.d("PersonalCards", personalCards.toString())
//                        personalCardsFromServer.postValue(personalCards)
//                    }
//
//                    override fun onCancelled(databaseError: DatabaseError) {
//                        isDataFailed.postValue(Unit)
//                    }
//                })
//            }
//        }
//    }
}