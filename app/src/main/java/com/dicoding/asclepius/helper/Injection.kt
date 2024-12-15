package com.dicoding.asclepius.helper

import android.content.Context
import com.dicoding.asclepius.data.HistoryRepository
import com.dicoding.asclepius.data.local.room.HistoryDatabase

object Injection {
    fun provideRepository(context: Context) : HistoryRepository {
        val db = HistoryDatabase.getDatabase(context)
        val dao = db.historyDao()
        return HistoryRepository.getInstance(dao)
    }
}