package com.example.businesscards.data

data class UserModel(
    val id: String,
    val userName: String,
    val email: String,
    val cardsToShare: List<CardUiModel>,
    val sharedCards: List<CardUiModel>
)
