package com.example.myapplication.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DealEntity::class, CanvasEntity::class], version = 6, exportSchema = false)
abstract class AppDB : RoomDatabase() {
    abstract fun dealDao() : DealDao
    abstract fun canvasDao(): CanvasDao
}