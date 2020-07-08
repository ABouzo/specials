package com.bouzo.specials

import androidx.recyclerview.widget.RecyclerView
import com.bouzo.specials.datamodels.CanvasInfo
import com.bouzo.specials.datamodels.DealItem

interface DealsRecyclerAdapter {
    val adapter: RecyclerView.Adapter<*>
    var deals: List<DealItem>
    var canvasInfo: CanvasInfo
}