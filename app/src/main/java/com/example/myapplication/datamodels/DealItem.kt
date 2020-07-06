package com.example.myapplication.datamodels

data class DealItem(
    val dealImageUrl: String,
    val dealName: String,
    val width: Int,
    val height: Int,
    val originalPrice: String,
    val price: String
)