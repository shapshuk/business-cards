package com.example.businesscards.core.coreui.overlappingrecyclerview

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.businesscards.R

class OverlappingViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
    val userName: TextView = itemView.findViewById(R.id.user_name)
    val email: TextView = itemView.findViewById(R.id.email)
    val phoneNumber: TextView = itemView.findViewById(R.id.phone_number)
}