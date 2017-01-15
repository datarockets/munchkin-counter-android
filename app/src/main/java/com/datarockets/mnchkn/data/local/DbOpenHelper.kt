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
        if (previousVersion > 1) {
            db.execSQL("ALTER TABLE "
                    + Db.PlayerTable.TABLE_NAME + " ADD COLUMN "
                    + Db.PlayerTable.KEY_PLAYER_COLOR + " TEXT")
            //            if (!isTableEmpty(db, TABLE_PLAYERS)) {
            //                addColorsToUpdatedPlayers(db);
            //            }
        }
    }

    companion object {

        val DATABASE_NAME = "players_db"
        val DATABASE_VERSION = 6
    }


}
