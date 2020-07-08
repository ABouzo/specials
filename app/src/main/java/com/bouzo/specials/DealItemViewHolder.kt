package com.bouzo.specials

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bouzo.specials.views.ResizableDealCard

class DealItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val layout: ResizableDealCard = view as ResizableDealCard
    val dealImage: ImageView = view.findViewById(R.id.deal_image)
    val dealName: TextView = view.findViewById(R.id.deal_name)
    val dealOriginalPrice: TextView = view.findViewById(R.id.deal_original)
    val dealPrice: TextView = view.findViewById(R.id.deal_price)
}