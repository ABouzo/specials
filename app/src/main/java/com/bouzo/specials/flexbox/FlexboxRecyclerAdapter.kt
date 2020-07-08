package com.bouzo.specials.flexbox

import android.content.Context
import android.content.res.Resources
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bouzo.specials.DealItemViewHolder
import com.bouzo.specials.DealsRecyclerAdapter
import com.bouzo.specials.R
import com.bouzo.specials.datamodels.CanvasInfo
import com.bouzo.specials.datamodels.DealItem
import com.bouzo.specials.views.ResizableDealCard
import com.google.android.flexbox.FlexboxLayoutManager
import com.squareup.picasso.Picasso
import kotlin.math.min

class FlexboxRecyclerAdapter internal constructor(
    context: Context,
    private val picasso: Picasso
) : RecyclerView.Adapter<DealItemViewHolder>(), DealsRecyclerAdapter {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val screenWidth = Resources.getSystem().displayMetrics.widthPixels

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealItemViewHolder {
        val layout: View = inflater.inflate(R.layout.deal_card, parent, false)
        return DealItemViewHolder(layout)
    }

    override fun getItemCount(): Int = deals.size

    override fun onBindViewHolder(holder: DealItemViewHolder, position: Int) {
        val dealItem = deals[position]
        val unitsInPixels = (screenWidth / canvasInfo.canvasUnit)

        holder.apply {
            (layout.layoutParams as FlexboxLayoutManager.LayoutParams).apply {
                width = (dealItem.width * unitsInPixels) - (marginLeft + marginRight)
                height = (dealItem.height * unitsInPixels) - (marginTop + marginBottom)
            }
            picasso.load(dealItem.dealImageUrl).into(dealImage)
        }

        chooseVariant(holder, dealItem)
        populateData(holder, dealItem)
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

    companion object {
        fun chooseVariant(holder: DealItemViewHolder, dealItem: DealItem) = holder.apply {
            layout.variant = when (min(dealItem.width, dealItem.height)) {
                in 0..3 -> ResizableDealCard.SIZE_TINY
                4, 5 -> ResizableDealCard.SIZE_SMALL
                in 6..10 -> ResizableDealCard.SIZE_MEDIUM
                else -> ResizableDealCard.SIZE_BIG
            }
        }

        fun populateData(holder: DealItemViewHolder, dealItem: DealItem) = holder.apply {

            dealName.text = dealItem.dealName
            dealPrice.text = dealItem.price
            dealOriginalPrice.text = dealItem.originalPrice
            dealOriginalPrice.paintFlags =
                dealOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

}