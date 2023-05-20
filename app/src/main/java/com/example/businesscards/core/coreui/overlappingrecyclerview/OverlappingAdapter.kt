package com.example.businesscards.core.coreui.overlappingrecyclerview

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.businesscards.R
import com.example.businesscards.data.CardUiModel

class OverlappingAdapter(private var items: MutableList<CardUiModel>, private val onClickListener: (position: Int) -> Unit) : RecyclerView.Adapter<OverlappingViewHolder>() {

    fun setItems(newList: MutableList<CardUiModel>) {
        items.clear()
        items.addAll(newList)
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OverlappingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false)
        return OverlappingViewHolder(view)
    }

    override fun onBindViewHolder(holder: OverlappingViewHolder, position: Int) {
        holder.userName.text = items[position].userName
        holder.email.text = items[position].email
        holder.phoneNumber.text = items[position].phoneNumber
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
}