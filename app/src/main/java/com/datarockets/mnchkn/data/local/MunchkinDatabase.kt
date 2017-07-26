package com.datarockets.mnchkn.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.datarockets.mnchkn.data.entity.GameEntity
import com.datarockets.mnchkn.data.entity.PlayerEntity

@Database(version = 7, entities = arrayOf(PlayerEntity::class, GameEntity::class))
abstract class MunchkinDatabase : RoomDatabase() {

    abstract fun gameDao(): GameDao

    abstract fun playerDao(): PlayerDao
}