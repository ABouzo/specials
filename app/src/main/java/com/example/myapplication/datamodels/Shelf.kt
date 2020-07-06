package com.example.myapplication.datamodels

class Shelf(private val maxWeight: Int, dealItem: DealItem) : Collection<DealItem> {
    private val items: MutableList<DealItem> = mutableListOf(dealItem)

    private var weight = dealItem.width
    var padding = (maxWeight - weight) / 2

    fun putItem(item: DealItem): Boolean {
        if (weight + item.width > maxWeight) return false
        items.add(item)
        weight += item.width
        padding = (maxWeight - weight) / 2
        return true
    }

    override val size: Int
        get() = items.size

    override fun contains(element: DealItem): Boolean = items.contains(element)

    override fun containsAll(elements: Collection<DealItem>): Boolean = items.containsAll(elements)

    override fun isEmpty(): Boolean = items.isEmpty()

    override fun iterator(): Iterator<DealItem> = items.iterator()

}