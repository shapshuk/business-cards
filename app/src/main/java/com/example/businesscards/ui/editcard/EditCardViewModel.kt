package com.example.businesscards.ui.editcard

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditCardViewModel @Inject constructor(
    private val databaseReference: DatabaseReference
) : ViewModel() {

    var imageUri : Uri? = null
    val showSuccessMessageLiveData = MutableLiveData<Unit>()
    val navigateBackToMainScreenLiveData = MutableLiveData<Unit>()

    fun createCard(
        currentCardId : String,
        userId: String,
        userName: String,
        email: String,
        phoneNumber: String,
        jobPosition: String,
        imageUrl: String
    ) {

        val cardData = hashMapOf(
            "userName" to userName,
            "email" to email,
            "phoneNumber" to phoneNumber,
            "jobPosition" to jobPosition,
            "imageUrl" to imageUrl
        )

        databaseReference.child("Cards").child(currentCardId).setValue(cardData)
        handleSuccess()
    }

    private fun handleSuccess() {
        viewModelScope.launch {
            showSuccessMessageLiveData.postValue(Unit)
            delay(1000)
            navigateBackToMainScreenLiveData.postValue(Unit)
        }
    }
}