package com.dicoding.asclepius.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dicoding.asclepius.data.local.entity.HistoryEntity

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(historyEntity : HistoryEntity)

    @Update
    suspend fun updateEvents(history : HistoryEntity)

    @Query("SELECT * FROM history_table ORDER BY timestamp DESC")
    fun getAllHistory(): LiveData<List<HistoryEntity>>

    @Query("DELETE FROM history_table")
    suspend fun deleteAll()

}