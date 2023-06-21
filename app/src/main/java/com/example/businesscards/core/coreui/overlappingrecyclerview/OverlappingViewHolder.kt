package com.example.businesscards.core.coreui.overlappingrecyclerview

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.businesscards.R
import com.example.businesscards.core.coreui.invisible
import com.example.businesscards.data.CardUiModel
import com.example.businesscards.databinding.CardItemBinding

class OverlappingViewHolder(
    private val binding: CardItemBinding,
    private val nfcClickAction: (CardUiModel) -> Unit,
    private val deleteClickAction: (CardUiModel) -> Unit,
    private val editClickAction: (CardUiModel) -> Unit,
    private val bookmarkClickAction: (CardUiModel) -> Unit,
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
            deleteButton.setOnClickListener {
                deleteClickAction.invoke(item)
            }
            editButton.setOnClickListener {
                editClickAction.invoke(item)
            }
            bookmarkButton.setOnClickListener {
                bookmarkClickAction.invoke(item)
            }

            if (!item.isPersonalCard) {
                invisible(nfcButton)
                Glide.with(binding.root)
                    .load(R.drawable.phone_svgrepo_com)
                    .into(binding.editButton)

                Glide.with(binding.root)
                    .load(R.drawable.ic_email_button)
                    .into(binding.bookmarkButton)
            }
        }
    }
}