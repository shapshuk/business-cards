package com.example.businesscards.core.coreui.overlappingrecyclerview

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.businesscards.R
import com.example.businesscards.data.CardUiModel
import com.example.businesscards.databinding.CardItemBinding

class OverlappingViewHolder(
    private val binding: CardItemBinding,
    private val nfcClickAction: (CardUiModel) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
//    val userName: TextView = binding.userName
//    val email: TextView = itemView.findViewById(R.id.email)
//    val phoneNumber: TextView = itemView.findViewById(R.id.phone_number)

    fun bindView(item: CardUiModel) {
        with(binding) {
            userName.text = item.userName
            email.text = item.email
            phoneNumber.text = item.phoneNumber
            nfcButton.setOnClickListener {
                nfcClickAction.invoke(item)
            }
        }
    }
}