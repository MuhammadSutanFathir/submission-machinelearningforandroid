package com.dicoding.asclepius.data

import com.dicoding.asclepius.data.local.entity.HistoryEntity
import com.dicoding.asclepius.data.local.room.HistoryDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HistoryRepository private constructor(
    private val historyDao : HistoryDao
){
    suspend fun insertHistory(history: HistoryEntity) {
        return withContext(Dispatchers.IO){
            historyDao.insertEvents(history)
        }
    }


    fun getAllHistory() = historyDao.getAllHistory()

    companion object {
        @Volatile
        private var INSTANCE: HistoryRepository? = null

        fun getInstance(
            historyDao: HistoryDao
        ): HistoryRepository = INSTANCE ?: synchronized(this) {
            INSTANCE ?: HistoryRepository(historyDao)
        }.also { INSTANCE = it }
    }
}