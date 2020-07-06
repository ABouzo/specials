package com.example.myapplication

import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView

class ShelfViewHolder(view: LinearLayout, val dealItems: List<DealItemViewHolder>) : RecyclerView.ViewHolder(view) {
    val layout: LinearLayout = view.findViewById(R.id.shelf_linear_layout)
}