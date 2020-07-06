package com.example.myapplication

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class DealItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val layout: ConstraintLayout = view.findViewById(R.id.deal_container)
    val dealImage: ImageView = view.findViewById(R.id.deal_image)
    val dealName: TextView = view.findViewById(R.id.deal_name)
    val dealOriginalPrice: TextView = view.findViewById(R.id.deal_original)
    val dealPrice: TextView = view.findViewById(R.id.deal_price)
}