package com.datarockets.mnchknlvlcntr.data.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "players")
data class PlayerEntity constructor(
        @PrimaryKey(autoGenerate = false)
        val id: Long,
        @ColumnInfo(name = "name")
        val name: String,
        @ColumnInfo(name = "level")
        val level: Int,
        @ColumnInfo(name = "strength")
        val strength: Int,
        @ColumnInfo(name = "color")
        val color: String,
        @ColumnInfo(name = "position")
        val position: Int,
        @ColumnInfo(name = "is_playing")
        val isPlaying: Boolean
)
