package com.example.businesscards.mainscreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.businesscards.R

class MyRecyclerViewAdapter(private val number: Int):
    RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val largeTextView : TextView = itemView.findViewById(R.id.large_text_view)
        val smallTextView : TextView = itemView.findViewById(R.id.small_text_view)
    }

    override fun getItemCount(): Int {
        return number
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.largeTextView.text = "Business Card"
        holder.smallTextView.text = "Additional information about user"
    }
}