package com.example.myapplication.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DealEntity::class, CanvasEntity::class], version = 2)
abstract class AppDB : RoomDatabase() {
    abstract fun dealDao() : DealDao
    abstract fun canvasDao(): CanvasDao
}