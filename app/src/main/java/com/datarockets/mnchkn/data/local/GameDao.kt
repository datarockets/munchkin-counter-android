package com.datarockets.mnchkn.data.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.datarockets.mnchkn.data.entity.GameEntity
import io.reactivex.Maybe

@Dao
interface GameDao {

    @Query("SELECT * FROM game")
    fun getGameSteps(): List<GameEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setGameStep(gameEntity: GameEntity)

    @Query("DELETE FROM game")
    fun deleteGameSteps()
}