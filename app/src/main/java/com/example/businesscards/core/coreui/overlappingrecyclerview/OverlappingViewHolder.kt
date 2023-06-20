package com.example.businesscards.core.coreui.overlappingrecyclerview

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.businesscards.R
import com.example.businesscards.data.CardUiModel
import com.example.businesscards.databinding.CardItemBinding

class OverlappingViewHolder(
    private val binding: CardItemBinding,
    private val nfcClickAction: (CardUiModel) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bindView(item: CardUiModel) {
        with(binding) {
            userName.text = item.userName
            email.text = item.email
            phoneNumber.text = item.phoneNumber
            jobPosition.text = item.jobPosition

            Glide.with(binding.root)
                .load(item.imageUrl)
                .placeholder(R.drawable.ic_account_circle_orange)
                .circleCrop()
                .into(userImage)

            nfcButton.setOnClickListener {
                nfcClickAction.invoke(item)
            }
        }
    }
}