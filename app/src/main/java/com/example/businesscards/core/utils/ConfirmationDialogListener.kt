package com.example.businesscards.core.utils

interface ConfirmationDialogListener {
    fun onPositiveButtonClicked(cardId: String)
    fun onNegativeButtonClicked()
}