package com.example.myapplication.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deal")
data class DealEntity(
    val dealName: String,
    val dealUrl: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}