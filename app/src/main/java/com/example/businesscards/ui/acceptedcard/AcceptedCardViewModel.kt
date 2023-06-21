package com.example.businesscards.ui.acceptedcard

import androidx.lifecycle.ViewModel
import com.example.businesscards.data.CardUiModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

class AcceptedCardViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val databaseReference: DatabaseReference
) : ViewModel() {

    fun saveAcceptedCard(cardId: String) {
        val userId = firebaseAuth.currentUser!!.uid
//        val cardId = databaseReference.child("Users").child(userId).child("OtherCards").push().key

//        val cardData = hashMapOf(
//            "userName" to acceptedCard.userName,
//            "email" to acceptedCard.email,
//            "phoneNumber" to acceptedCard.phoneNumber,
//            "jobPosition" to acceptedCard.jobPosition,
//            "imageUrl" to acceptedCard.imageUrl
//        )

        if (cardId != null) {
//            databaseReference.child("Cards").child(cardId).setValue(cardData)
            val personalCardsRef = databaseReference.child("Users").child(userId).child("OtherCards")
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
//                            handleSuccess()
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
}