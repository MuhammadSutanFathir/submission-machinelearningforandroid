package com.dicoding.asclepius.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "history_table")
@Parcelize
data class HistoryEntity(
    @PrimaryKey
    val id : Int,

    @field:ColumnInfo(name = "imageUri")
    val imageUri: String,

    @field:ColumnInfo(name = "label")
    val label: String,

    @field:ColumnInfo(name = "confidenceScore")
    val confidenceScore: Float,

    @field:ColumnInfo(name = "timestamp")
    val timestamp: Long = System.currentTimeMillis()

): Parcelable