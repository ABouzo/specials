package com.example.myapplication.shelfs

import android.content.Context
import android.content.res.Resources
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.DealItemViewHolder
import com.example.myapplication.DealsRecyclerAdapter
import com.example.myapplication.R
import com.example.myapplication.datamodels.CanvasInfo
import com.example.myapplication.datamodels.DealItem
import com.squareup.picasso.Picasso

class ShelfRecyclerAdapter internal constructor(
    context: Context,
    private val picasso: Picasso
) : RecyclerView.Adapter<ShelfViewHolder>(), DealsRecyclerAdapter {

    override var canvasInfo: CanvasInfo = CanvasInfo(1)
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val _deals: ShelfList
        get() = ShelfList(
            canvasInfo.canvasUnit,
            deals
        )

    override var deals: List<DealItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val screenWidth = Resources.getSystem().displayMetrics.widthPixels

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShelfViewHolder {
        val layoutShelf: LinearLayout =
            inflater.inflate(R.layout.layout_shelf, parent, false) as LinearLayout

        val dealsVH = mutableListOf<DealItemViewHolder>()

        //This will generate enough deal views for every deal in a row to have a width of 1
        for (unit in 0..canvasInfo.canvasUnit) {
            val dealLayout = inflater.inflate(R.layout.deal_item, layoutShelf, false)
            dealLayout.layoutParams.apply {
                height = ViewGroup.LayoutParams.WRAP_CONTENT
                width = ViewGroup.LayoutParams.WRAP_CONTENT
            }

            dealLayout.visibility = View.GONE
            layoutShelf.addView(dealLayout)
            dealsVH.add(DealItemViewHolder(dealLayout))
        }

        return ShelfViewHolder(
            layoutShelf,
            dealsVH.toList()
        )
    }

    override fun onBindViewHolder(holder: ShelfViewHolder, position: Int) {
        val current = _deals.shelfList[position]
        val unitsInPixels = (screenWidth / canvasInfo.canvasUnit)
        holder.dealItems.forEach { it.layout.visibility = View.GONE }
        current.forEachIndexed { index, dealItem ->
            val dealVH = holder.dealItems[index]
            dealVH.apply {
                layout.layoutParams.apply {
                    height = dealItem.height * unitsInPixels
                    width = dealItem.width * unitsInPixels
                }

                layout.visibility = View.VISIBLE

                dealName.text = dealItem.dealName
                dealPrice.text = dealItem.price
                dealOriginalPrice.text = dealItem.originalPrice
                dealOriginalPrice.paintFlags =
                    (dealOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG)
                picasso.load(dealItem.dealImageUrl)
                    .into(dealImage)
            }
        }
    }

    override fun getItemCount(): Int = _deals.shelfList.size

    override val adapter: RecyclerView.Adapter<*>
        get() = this
}