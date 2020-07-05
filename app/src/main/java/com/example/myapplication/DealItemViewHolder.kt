package com.example.myapplication

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DealItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val dealImage : ImageView = view.findViewById(R.id.deal_image)
    val dealName : TextView = view.findViewById(R.id.deal_name)
}