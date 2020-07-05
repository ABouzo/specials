package com.example.myapplication.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "canvas")
data class CanvasEntity(
    @PrimaryKey
    val id: Int,
    val canvasUnit: Int
)