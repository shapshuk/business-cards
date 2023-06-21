package com.example.businesscards.ui.createcard

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class CreateCardViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val databaseReference: DatabaseReference
) : ViewModel() {

    var imageUri: Uri? = null
    val showSuccessMessageLiveData = MutableLiveData<Unit>()
    val navigateBackToMainScreenLiveData = MutableLiveData<Unit>()

    fun createCard(
        userId: String,
        userName: String,
        email: String,
        phoneNumber: String,
        jobPosition: String,
        imageUrl: String
    ) {
        val cardId = databaseReference.child("Users").child(userId).child("PersonalCards").push().key

        val cardData = hashMapOf(
            "userName" to userName,
            "email" to email,
            "phoneNumber" to phoneNumber,
            "jobPosition" to jobPosition,
            "imageUrl" to imageUrl
        )

        if (cardId != null) {
            databaseReference.child("Cards").child(cardId).setValue(cardData)
//            databaseReference.child("Users").child(userId).child("PersonalCards").push().setValue(cardId)

            val personalCardsRef = databaseReference.child("Users").child(userId).child("PersonalCards")
            personalCardsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val currentCards = try {
                        dataSnapshot.value as MutableList<*>
                    } catch (e: Exception) {
                        mutableListOf<String>()
                    }
                    val updatedCards = currentCards.toMutableList()
                    updatedCards.add(cardId)

                    personalCardsRef.setValue(updatedCards)
                        .addOnSuccessListener {
                            handleSuccess()
                        }
                        .addOnFailureListener { exception ->
                            // An error occurred while adding the new card ID
                        }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

    private fun handleSuccess() {
        viewModelScope.launch {
            showSuccessMessageLiveData.postValue(Unit)
            delay(1000)
            navigateBackToMainScreenLiveData.postValue(Unit)
        }
    }
}