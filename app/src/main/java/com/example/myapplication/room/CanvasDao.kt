package com.example.myapplication.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface CanvasDao {
    companion object {
        const val DEFAULT_ID = 0;
    }
    @Insert(onConflict = REPLACE)
    fun save(canvasEntity: CanvasEntity)

    @Query("SELECT * FROM canvas WHERE id = $DEFAULT_ID LIMIT 1")
    fun getCanvas(): LiveData<CanvasEntity>
}