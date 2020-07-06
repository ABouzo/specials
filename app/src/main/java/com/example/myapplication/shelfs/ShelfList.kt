package com.example.myapplication.shelfs

import com.example.myapplication.datamodels.DealItem

class ShelfList(private val maxWidth: Int, dealItems: List<DealItem>) {
    private val _shelfList: MutableList<Shelf> = mutableListOf()

    val shelfList: List<Shelf>
        get() = _shelfList.toList()

    val allDealItem: List<DealItem> = dealItems

    init {
        if (dealItems.isNotEmpty()) {
            _shelfList.add(
                Shelf(
                    maxWidth,
                    dealItems.first()
                )
            )
            dealItems.minus(dealItems.first()).forEach {
                put(it)
            }
        }
    }

    private fun put(item: DealItem): Int {
        //Try to add to the last item
        val didInsert: Boolean = _shelfList.last().putItem(item)
        if (!didInsert) _shelfList.add(
            Shelf(maxWidth, item)
        )

        return _shelfList.size
    }

}