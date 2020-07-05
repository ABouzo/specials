package com.example.myapplication.restapi.models

import com.google.gson.annotations.SerializedName

data class ManagerSpecial(
    @SerializedName("display_name")
    val displayName: String,

    val height: Int,

    val imageUrl: String,

    @SerializedName("original_price")
    val originalPrice: String,

    val price: String,
    val width: Int
)