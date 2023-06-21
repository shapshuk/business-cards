package com.example.businesscards.ui.sharecard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShareCardViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    val navigateToAcceptedCardNeeded = MutableLiveData<Unit>()

    fun startCountDown() {
        viewModelScope.launch {
            delay(3000)
            navigateToAcceptedCardNeeded.postValue(Unit)
        }
    }
}