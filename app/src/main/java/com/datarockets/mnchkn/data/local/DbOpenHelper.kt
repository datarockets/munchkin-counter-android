package com.datarockets.mnchkn.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

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
        when (previousVersion) {
            1 -> addColorColumnToPlayersTable(db)
            6 -> addPositionIsPlayingColumnToPlayersTable(db)
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
                + Db.PlayerTable.KEY_PLAYER_POSITION + " INTEGER,"
                + Db.PlayerTable.KEY_PLAYER_IS_PLAYING + " INTEGER")
        db.setTransactionSuccessful()
        db.endTransaction()
    }

    fun addColorColumnToPlayersTable(db: SQLiteDatabase) {
        db.beginTransaction()
        db.execSQL("ALTER TABLE "
                + Db.PlayerTable.TABLE_NAME + " ADD COLUMN "
                + Db.PlayerTable.KEY_PLAYER_COLOR + " TEXT")
//                        if (!isTableEmpty(db, TABLE_PLAYERS)) {
//                            addColorsToUpdatedPlayers(db);
//                        }
        db.setTransactionSuccessful()
        db.endTransaction()
    }

}
