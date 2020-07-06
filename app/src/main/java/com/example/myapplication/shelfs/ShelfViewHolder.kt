package com.example.myapplication.shelfs

import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.DealItemViewHolder
import com.example.myapplication.R

class ShelfViewHolder(view: LinearLayout, val dealItems: List<DealItemViewHolder>) : RecyclerView.ViewHolder(view) {
    val layout: LinearLayout = view.findViewById(R.id.shelf_linear_layout)
}