package com.example.myapplication.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface DealDao {
    // NOTE: Maybe 'deal' objects can be used across different types of deals?
    // Or as any type of item that is being sold
    @Insert(onConflict = REPLACE)
    fun save(vararg dealEntity: DealEntity)

    @Query("SELECT * FROM deal")
    fun loadAll() : LiveData<List<DealEntity>>

    @Query("DELETE FROM deal")
    fun clearDb()
}