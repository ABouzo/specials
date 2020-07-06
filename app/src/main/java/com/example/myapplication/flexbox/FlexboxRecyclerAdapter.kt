package com.example.myapplication.flexbox

import android.content.Context
import android.content.res.Resources
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.DealItemViewHolder
import com.example.myapplication.DealsRecyclerAdapter
import com.example.myapplication.R
import com.example.myapplication.datamodels.CanvasInfo
import com.example.myapplication.datamodels.DealItem
import com.google.android.flexbox.FlexboxLayout
import com.google.android.flexbox.FlexboxLayoutManager
import com.squareup.picasso.Picasso

class FlexboxRecyclerAdapter internal constructor(
    context: Context,
    private val picasso: Picasso
) : RecyclerView.Adapter<DealItemViewHolder>(), DealsRecyclerAdapter {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val screenWidth = Resources.getSystem().displayMetrics.widthPixels

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealItemViewHolder {
        val layout: View = inflater.inflate(R.layout.deal_item, parent, false)
        return DealItemViewHolder(layout)
    }

    override fun getItemCount(): Int = deals.size

    override fun onBindViewHolder(holder: DealItemViewHolder, position: Int) {
        val dealItem = deals[position]
        val unitsInPixels = (screenWidth / canvasInfo.canvasUnit)

        holder.apply {
            (layout.layoutParams as FlexboxLayoutManager.LayoutParams).apply {
                //flexBasisPercent = dealItem.width.toFloat() / canvasInfo.canvasUnit.toFloat()
                width = dealItem.width * unitsInPixels
                height = dealItem.height * unitsInPixels
            }
            dealName.text = dealItem.dealName
            dealPrice.text = dealItem.price
            dealOriginalPrice.text = dealItem.originalPrice
            dealOriginalPrice.paintFlags = dealOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            picasso.load(dealItem.dealImageUrl).into(dealImage)
        }
    }

    override val adapter: RecyclerView.Adapter<*>
        get() = this

    override var deals: List<DealItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override var canvasInfo: CanvasInfo = CanvasInfo(1)
        set(value) {
            field = value
            notifyDataSetChanged()
        }
}