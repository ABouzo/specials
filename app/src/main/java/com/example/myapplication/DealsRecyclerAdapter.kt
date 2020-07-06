package com.example.myapplication

import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.datamodels.CanvasInfo
import com.example.myapplication.datamodels.DealItem

interface DealsRecyclerAdapter {
    val adapter: RecyclerView.Adapter<*>
    var deals: List<DealItem>
    var canvasInfo: CanvasInfo
}