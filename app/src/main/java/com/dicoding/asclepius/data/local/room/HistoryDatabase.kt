package com.dicoding.asclepius.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.asclepius.data.local.entity.HistoryEntity

@Database(entities = [HistoryEntity::class], version = 2, exportSchema = false)
abstract class HistoryDatabase : RoomDatabase() {
    abstract fun historyDao() : HistoryDao

    companion object {
        @Volatile
        private var INSTANCE :HistoryDatabase? = null

        fun getDatabase(context: Context) : HistoryDatabase {
            if(INSTANCE == null) {
                synchronized(HistoryDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, HistoryDatabase::class.java, "history_table").build()
                }
            }
            return INSTANCE as HistoryDatabase
        }
    }
}