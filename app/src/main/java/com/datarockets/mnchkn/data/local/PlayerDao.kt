package com.datarockets.mnchkn.data.local

import android.arch.persistence.room.*
import com.datarockets.mnchkn.data.entity.PlayerEntity

@Dao
interface PlayerDao {

    @Query("SELECT * FROM players WHERE id = :id LIMIT 1")
    fun getPlayerById(id: Long): PlayerEntity

    @Query("SELECT * FROM players ORDER BY position ASC")
    fun getPlayers(): List<PlayerEntity>

    @Query("SELECT * FROM players WHERE is_playing == 1 ORDER BY position DESC")
    fun getPlayingPlayersByPosition(): List<PlayerEntity>

    @Query("SELECT * FROM players WHERE is_playing == 1 ORDER BY level DESC")
    fun getPlayingPlayersByLevel(): List<PlayerEntity>

    @Query("SELECT * FROM players WHERE is_playing == 1 ORDER BY strength DESC")
    fun getPlayingPlayersByStrength(): List<PlayerEntity>

    @Query("SELECT * FROM players WHERE is_playing == 1 ORDER BY level DESC, strength DESC")
    fun getPlayingPlayersByTotal(): List<PlayerEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setPlayer(playerEntity: PlayerEntity): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updatePlayer(vararg playerEntity: PlayerEntity)

    @Query("DELETE FROM players WHERE id == :id")
    fun deletePlayerById(id: Long)
}