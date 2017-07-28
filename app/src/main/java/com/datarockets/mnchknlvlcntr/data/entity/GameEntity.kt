package com.datarockets.mnchknlvlcntr.data.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE

@Entity(tableName = "game",
        primaryKeys = arrayOf("player_id", "player_level", "player_strength"),
        foreignKeys = arrayOf(ForeignKey(
                entity = PlayerEntity::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("player_id"),
                onDelete = CASCADE,
                onUpdate = CASCADE)))
data class GameEntity constructor(
        @ColumnInfo(name = "player_id")
        val playerId: Long,
        @ColumnInfo(name = "player_level")
        val playerLevel: Int,
        @ColumnInfo(name = "player_strength")
        val playerStrength: Int
)
