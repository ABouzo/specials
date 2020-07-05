package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.datamodels.DealItem

class DealRecyclerAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<DealItemViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    var deals: List<DealItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealItemViewHolder {
        val dealView: View = inflater.inflate(R.layout.deal_item, parent, false)
        return DealItemViewHolder(dealView)
    }

    override fun onBindViewHolder(holder: DealItemViewHolder, position: Int) {
        val current = deals[position]
        //TODO("Replace placeholder)
        holder.dealName.text = "${current.dealName} with image ${current.dealImageUrl}"
    }

    override fun getItemCount(): Int = deals.size
}