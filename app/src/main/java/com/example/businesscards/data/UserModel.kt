package com.example.businesscards.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    val id: String,
    val userName: String,
    val email: String,
    val cardsToShare: List<CardUiModel>,
    val sharedCards: List<CardUiModel>
): Parcelable
