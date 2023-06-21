package com.example.businesscards.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CardUiModel (
    val cardId: String = "",
    val userName: String? = "",
    val email : String? = "",
    val imageUrl : String? = "",
    val phoneNumber : String? = "",
    val jobPosition : String? = "",
    var isPersonalCard : Boolean = true
): Parcelable