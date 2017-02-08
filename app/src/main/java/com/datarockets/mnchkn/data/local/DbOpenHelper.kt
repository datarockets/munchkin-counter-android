package com.datarockets.mnchkn.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.datarockets.mnchkn.data.models.Player
import com.datarockets.mnchkn.injection.ApplicationContext
import javax.inject.Inject



class DbOpenHelper
@Inject constructor(@ApplicationContext context: Context) :
        SQLiteOpenHelper(context, DbOpenHelper.DATABASE_NAME, null, DbOpenHelper.DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.beginTransaction()
        try {
            db.execSQL(Db.PlayerTable.CREATE)
            db.execSQL(Db.GameTable.CREATE)
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, previousVersion: Int, newVersion: Int) {
        if (previousVersion < 6) {
            addColorColumnToPlayersTable(db)
        }
        if (previousVersion < 7) {
            addPositionIsPlayingColumnToPlayersTable(db)
        }
    }

    companion object {
        val DATABASE_NAME = "players_db"
        val DATABASE_VERSION = 7
    }

    fun addPositionIsPlayingColumnToPlayersTable(db: SQLiteDatabase) {
        db.beginTransaction()
        db.execSQL("ALTER TABLE "
                + Db.PlayerTable.TABLE_NAME + " ADD COLUMN "
                + Db.PlayerTable.KEY_PLAYER_POSITION + " INTEGER")
        db.execSQL("ALTER TABLE "
                + Db.PlayerTable.TABLE_NAME + " ADD COLUMN "
                + Db.PlayerTable.KEY_PLAYER_IS_PLAYING + " INTEGER")
        if (!isTableEmpty(db, Db.PlayerTable.TABLE_NAME)) {
            addPositionsToPlayers(db)
        }
        db.setTransactionSuccessful()
        db.endTransaction()
    }

    fun addColorColumnToPlayersTable(db: SQLiteDatabase) {
        db.beginTransaction()
        db.execSQL("ALTER TABLE "
                + Db.PlayerTable.TABLE_NAME + " ADD COLUMN "
                + Db.PlayerTable.KEY_PLAYER_COLOR + " TEXT")
        if (!isTableEmpty(db, Db.PlayerTable.TABLE_NAME)) {
            addColorsToPlayers(db)
        }
        db.setTransactionSuccessful()
        db.endTransaction()
    }

    fun addColorsToPlayers(db: SQLiteDatabase) {
        val values = ContentValues()
        values.put(Db.PlayerTable.KEY_PLAYER_COLOR, "#3B1606")
        db.update(Db.PlayerTable.TABLE_NAME, values, null, null)
    }

    fun addPositionsToPlayers(db: SQLiteDatabase) {
        val players = mutableListOf<Player>()
        val query = String.format("SELECT * FROM %s", Db.PlayerTable.TABLE_NAME)
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            players.add(Db.PlayerTable.parseCursor(cursor))
        }
        players.forEachIndexed { index, player ->
            val contentValues = ContentValues()
            contentValues.put(Db.PlayerTable.KEY_PLAYER_POSITION, index)
            db.update(Db.PlayerTable.TABLE_NAME,
                    contentValues,
                    Db.PlayerTable.KEY_PLAYER_ID + " = ?",
                    arrayOf(player.id.toString()))
        }
    }

    private fun isTableEmpty(db: SQLiteDatabase, tableName: String): Boolean {
        val countQuery = "SELECT COUNT(*) FROM " + tableName
        val cursor = db.rawQuery(countQuery, null)
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        return count <= 0
    }

}
