package com.datarockets.mnchkn.data.local


import android.content.ContentValues
import android.database.Cursor

import com.datarockets.mnchkn.data.models.GameStep
import com.datarockets.mnchkn.data.models.Player

object Db {

    object PlayerTable {
        val TABLE_NAME = "players"

        val KEY_PLAYER_ID = "id"
        val KEY_PLAYER_NAME = "name"
        val KEY_PLAYER_LEVEL = "level"
        val KEY_PLAYER_STRENGTH = "strength"
        val KEY_PLAYER_COLOR = "color"
        val KEY_PLAYER_POSITION = "position"
        val KEY_PLAYER_IS_PLAYING = "is_playing"

        val CREATE = "CREATE TABLE " + TABLE_NAME +
                "(" +
                KEY_PLAYER_ID + " INTEGER PRIMARY KEY," +
                KEY_PLAYER_NAME + " TEXT," +
                KEY_PLAYER_LEVEL + " INTEGER," +
                KEY_PLAYER_STRENGTH + " INTEGER," +
                KEY_PLAYER_COLOR + " TEXT," +
                KEY_PLAYER_IS_PLAYING + " INTEGER," +
                KEY_PLAYER_POSITION + " INTEGER" +
                ")"

        fun parseCursor(cursor: Cursor): Player {
            val player = Player()
            player.id = cursor.getLong(cursor.getColumnIndexOrThrow(KEY_PLAYER_ID))
            player.name = cursor.getString(cursor.getColumnIndexOrThrow(KEY_PLAYER_NAME))
            player.color = cursor.getString(cursor.getColumnIndexOrThrow(KEY_PLAYER_COLOR))
            player.levelScore = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_PLAYER_LEVEL))
            player.strengthScore = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_PLAYER_STRENGTH))
            player.position = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_PLAYER_POSITION))
            player.playing = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_PLAYER_IS_PLAYING)) > 0
            return player
        }

        fun toContentValues(player: Player): ContentValues {
            val contentValues = ContentValues()
            contentValues.put(KEY_PLAYER_NAME, player.name)
            contentValues.put(KEY_PLAYER_COLOR, player.color)
            contentValues.put(KEY_PLAYER_LEVEL, player.levelScore)
            contentValues.put(KEY_PLAYER_STRENGTH, player.strengthScore)
            contentValues.put(KEY_PLAYER_IS_PLAYING, player.playing)
            return contentValues
        }

    }

    object GameTable {
        val TABLE_NAME = "game"

        val KEY_GAME_PLAYER_ID = "player_id"
        val KEY_GAME_PLAYER_LEVEL = "player_level"
        val KEY_GAME_PLAYER_STRENGTH = "player_strength"

        val CREATE = "CREATE TABLE " + TABLE_NAME +
                "(" +
                KEY_GAME_PLAYER_ID + " INTEGER REFERENCES " + PlayerTable.TABLE_NAME + "," +
                KEY_GAME_PLAYER_LEVEL + " INTEGER," +
                KEY_GAME_PLAYER_STRENGTH + " INTEGER" +
                ")"

        fun parseCursor(cursor: Cursor): GameStep {
            val gameStep = GameStep()
            gameStep.playerId = cursor.getLong(cursor.getColumnIndex(KEY_GAME_PLAYER_ID))
            gameStep.playerLevel = cursor.getInt(cursor.getColumnIndex(KEY_GAME_PLAYER_LEVEL))
            gameStep.playerStrength = cursor.getInt(cursor.getColumnIndex(KEY_GAME_PLAYER_STRENGTH))
            return gameStep
        }

        fun toContentValues(gameStep: GameStep): ContentValues {
            val contentValues = ContentValues()
            contentValues.put(KEY_GAME_PLAYER_ID, gameStep.playerId)
            contentValues.put(KEY_GAME_PLAYER_LEVEL, gameStep.playerLevel)
            contentValues.put(KEY_GAME_PLAYER_STRENGTH, gameStep.playerStrength)
            return contentValues
        }
    }

}
