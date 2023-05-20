package com.example.businesscards.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CardUiModel (
    val userName: String? = "",
    val email : String? = "",
    val imageUri : String? = "",
    val phoneNumber : String? = "",
): Parcelable