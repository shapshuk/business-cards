package com.example.businesscards.core.coreui.overlappingrecyclerview

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ExpandableListView.OnChildClickListener
import androidx.recyclerview.widget.RecyclerView
import com.example.businesscards.R
import com.example.businesscards.data.CardUiModel
import com.example.businesscards.databinding.CardItemBinding

class OverlappingAdapter(
    private var items: MutableList<CardUiModel>,
    private val onClickListener: (position: Int) -> Unit,
    private val nfcClickListener: (CardUiModel) -> Unit,
    private val deleteClickListener: (CardUiModel) -> Unit,
    private val editClickListener: (CardUiModel) -> Unit,
    private val bookmarkClickListener: (CardUiModel) -> Unit,
) : RecyclerView.Adapter<OverlappingViewHolder>() {

    fun setItems(newList: MutableList<CardUiModel>) {
        items.clear()
        items.addAll(newList)
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OverlappingViewHolder {
        val view = CardItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OverlappingViewHolder(view, nfcClickListener, deleteClickListener, editClickListener, bookmarkClickListener)
    }

    override fun onBindViewHolder(holder: OverlappingViewHolder, position: Int) {
        holder.bindView(items[position])
        holder.itemView.setOnClickListener {
            Log.d("Card", "Card was №${position+1} clicked")
            if (position+1 < itemCount) {
                onClickListener.invoke(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun removeItem(cardId: String) {
        items.removeIf { it.cardId == cardId }
        notifyDataSetChanged()
    }
}