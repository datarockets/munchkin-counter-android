package com.datarockets.mnchkn.data.local

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.datarockets.mnchkn.data.models.GameStep
import com.datarockets.mnchkn.data.models.Player
import com.squareup.sqlbrite.BriteDatabase
import com.squareup.sqlbrite.SqlBrite
import rx.Observable
import rx.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseHelper
@Inject constructor(dbOpenHelper: DbOpenHelper) {

    val briteDb: BriteDatabase

    init {
        val briteBuilder = SqlBrite.Builder()
                .logger { message -> Timber.tag("Database").v(message) }
        briteDb = briteBuilder.build().wrapDatabaseHelper(dbOpenHelper, Schedulers.immediate())
        briteDb.setLoggingEnabled(true)
    }

    fun setPlayer(player: Player): Observable<Player> {
        return Observable.create { subscriber ->
            val transaction = briteDb.newTransaction()
            try {
                val contentValues = Db.PlayerTable.toContentValues(player)
                val playerId = briteDb.insert(Db.PlayerTable.TABLE_NAME, contentValues)
                player.id = playerId
                subscriber.onNext(player)
                transaction.markSuccessful()
                subscriber.onCompleted()
            } finally {
                transaction.end()
            }
        }
    }

    fun getPlayer(playerId: Long): Observable<Player> {
        return Observable.create { subscriber ->
            val QUERY = String.format("SELECT * FROM %s WHERE %s = %s",
                    Db.PlayerTable.TABLE_NAME, Db.PlayerTable.KEY_PLAYER_ID, playerId)
            val cursor = briteDb.query(QUERY)
            while (cursor.moveToNext()) {
                subscriber.onNext(Db.PlayerTable.parseCursor(cursor))
            }
            cursor.close()
            subscriber.onCompleted()
        }
    }

    fun getPlayers(): Observable<Player> {
        return Observable.create { subscriber ->
            val query = String.format("SELECT * FROM %s ORDER BY %s ASC",
                    Db.PlayerTable.TABLE_NAME,
                    Db.PlayerTable.KEY_PLAYER_POSITION)
            val cursor = briteDb.query(query)
            while (cursor.moveToNext()) {
                Timber.d(cursor.getInt(cursor.getColumnIndexOrThrow(Db.PlayerTable.KEY_PLAYER_POSITION)).toString())
                subscriber.onNext(Db.PlayerTable.parseCursor(cursor))
            }
            cursor.close()
            subscriber.onCompleted()
        }
    }

    fun getPlayingPlayers(): Observable<Player> {
        return Observable.create { subscriber ->
            val query = String.format("SELECT * FROM %s WHERE %s = %s ORDER BY %s ASC",
                    Db.PlayerTable.TABLE_NAME,
                    Db.PlayerTable.KEY_PLAYER_IS_PLAYING, 1,
                    Db.PlayerTable.KEY_PLAYER_POSITION)
            val cursor = briteDb.query(query)
            while (cursor.moveToNext()) {
                subscriber.onNext(Db.PlayerTable.parseCursor(cursor))
            }
            cursor.close()
            subscriber.onCompleted()
        }
    }

    fun getPlayedPlayersByLevel(): Observable<Player> {
        return Observable.create { subscriber ->
            val query = String.format("SELECT * FROM %s WHERE %s = %s ORDER BY %s DESC",
                    Db.PlayerTable.TABLE_NAME,
                    Db.PlayerTable.KEY_PLAYER_IS_PLAYING, 1,
                    Db.PlayerTable.KEY_PLAYER_LEVEL)
            val cursor = briteDb.query(query)
            while (cursor.moveToNext()) {
                subscriber.onNext(Db.PlayerTable.parseCursor(cursor))
            }
            cursor.close()
            subscriber.onCompleted()
        }
    }

    fun getPlayedPlayersByStrength(): Observable<Player> {
        return Observable.create { subscriber ->
            val query = String.format("SELECT * FROM %s WHERE %s = %s ORDER BY %s DESC",
                    Db.PlayerTable.TABLE_NAME,
                    Db.PlayerTable.KEY_PLAYER_IS_PLAYING, 1,
                    Db.PlayerTable.KEY_PLAYER_STRENGTH)
            val cursor = briteDb.query(query)
            while (cursor.moveToNext()) {
                subscriber.onNext(Db.PlayerTable.parseCursor(cursor))
            }
            cursor.close()
            subscriber.onCompleted()
        }
    }

    fun getPlayedPlayersByTotal(): Observable<Player> {
        return Observable.create { subscriber ->
            val query = String.format("SELECT * FROM %s WHERE %s = %s ORDER BY %s DESC, %s DESC",
                    Db.PlayerTable.TABLE_NAME,
                    Db.PlayerTable.KEY_PLAYER_IS_PLAYING, 1,
                    Db.PlayerTable.KEY_PLAYER_LEVEL,
                    Db.PlayerTable.KEY_PLAYER_STRENGTH)
            val cursor = briteDb.query(query)
            while (cursor.moveToNext()) {
                subscriber.onNext(Db.PlayerTable.parseCursor(cursor))
            }
            cursor.close()
            subscriber.onCompleted()
        }
    }

    fun updatePlayersPositions(): Observable<Void> {
        return Observable.create { subscriber ->
            val players = mutableListOf<Player>()
            val query = String.format("SELECT * FROM %s ORDER BY %s ASC",
                    Db.PlayerTable.TABLE_NAME, Db.PlayerTable.KEY_PLAYER_POSITION)
            val cursor = briteDb.query(query)
            while (cursor.moveToNext()) {
                players.add(Db.PlayerTable.parseCursor(cursor))
            }
            for (index in players.indices) {
                Timber.d(index.toString())
                val transaction = briteDb.newTransaction()
                try {
                    val contentValues = ContentValues()
                    contentValues.put(Db.PlayerTable.KEY_PLAYER_POSITION, index)
                    briteDb.update(Db.PlayerTable.TABLE_NAME,
                            contentValues,
                            Db.PlayerTable.KEY_PLAYER_ID + " = ?",
                            players[index].id.toString())
                    transaction.markSuccessful()
                } finally {
                    transaction.end()
                }
            }
            subscriber.onCompleted()
        }
    }

    fun deletePlayer(playerId: Long): Observable<Void> {
        return Observable.create { subscriber ->
            val transaction = briteDb.newTransaction()
            try {
                briteDb.delete(
                        Db.PlayerTable.TABLE_NAME,
                        Db.PlayerTable.KEY_PLAYER_ID + " = ?",
                        playerId.toString())
                transaction.markSuccessful()
                subscriber.onCompleted()
            } finally {
                transaction.end()
            }
        }
    }

    fun updatePlayerName(playerId: Long, playerName: String): Observable<Void> {
        return Observable.create { subscriber ->
            val transaction = briteDb.newTransaction()
            try {
                val contentValues = ContentValues().apply {
                    put(Db.PlayerTable.KEY_PLAYER_NAME, playerName)
                }
                briteDb.update(Db.PlayerTable.TABLE_NAME,
                        contentValues,
                        Db.PlayerTable.KEY_PLAYER_ID + " = ?", playerId.toString())
                transaction.markSuccessful()
                subscriber.onCompleted()
            } finally {
                transaction.end()
            }
        }
    }

    fun updatePlayerScores(playerId: Long, levelScore: Int, strengthScore: Int): Observable<Void> {
        return Observable.create { subscriber ->
            val transaction = briteDb.newTransaction()
            try {
                val contentValues = ContentValues().apply {
                    put(Db.PlayerTable.KEY_PLAYER_LEVEL, levelScore)
                    put(Db.PlayerTable.KEY_PLAYER_STRENGTH, strengthScore)
                }
                briteDb.update(Db.PlayerTable.TABLE_NAME,
                        contentValues,
                        Db.PlayerTable.KEY_PLAYER_ID + " = ?", playerId.toString())
                transaction.markSuccessful()
                subscriber.onCompleted()
            } finally {
                transaction.end()
            }
        }
    }

    fun markPlayerPlaying(playerId: Long, isPlaying: Boolean): Observable<Void> {
        return Observable.create { subscriber ->
            val transaction = briteDb.newTransaction()
            try {
                val contentValues = ContentValues()
                contentValues.put(Db.PlayerTable.KEY_PLAYER_IS_PLAYING, isPlaying)
                briteDb.update(Db.PlayerTable.TABLE_NAME,
                        contentValues,
                        Db.PlayerTable.KEY_PLAYER_ID + " = ?", playerId.toString())
                transaction.markSuccessful()
                subscriber.onCompleted()
            } finally {
                transaction.end()
            }
        }
    }

    fun changePlayersPositions(movedPlayerId: Long,
                               replacedPlayerId: Long,
                               movedPosition: Int,
                               replacedPosition: Int): Observable<Void> {
        return Observable.create { subscriber ->
            val transaction = briteDb.newTransaction()
            try {
                val contentValues = ContentValues()
                contentValues.put(Db.PlayerTable.KEY_PLAYER_POSITION, replacedPosition)
                val contentValues2 = ContentValues()
                contentValues2.put(Db.PlayerTable.KEY_PLAYER_POSITION, movedPosition)
                briteDb.update(Db.PlayerTable.TABLE_NAME,
                        contentValues, SQLiteDatabase.CONFLICT_REPLACE,
                        Db.PlayerTable.KEY_PLAYER_ID + " = ?", movedPlayerId.toString())
                briteDb.update(Db.PlayerTable.TABLE_NAME,
                        contentValues2, SQLiteDatabase.CONFLICT_REPLACE,
                        Db.PlayerTable.KEY_PLAYER_ID + " = ?", replacedPlayerId.toString())
                transaction.markSuccessful()
                subscriber.onCompleted()
            } finally {
                transaction.end()
            }
        }
    }

    fun setGameStep(gameStep: GameStep): Observable<Void> {
        return Observable.create { subscriber ->
            val transaction = briteDb.newTransaction()
            try {
                val contentValues = Db.GameTable.toContentValues(gameStep)
                briteDb.insert(Db.GameTable.TABLE_NAME, contentValues)
                transaction.markSuccessful()
                subscriber.onCompleted()
            } finally {
                transaction.end()
            }
        }
    }

    fun clearPlayerStats(): Observable<Void> {
        return Observable.create { subscriber ->
            val transaction = briteDb.newTransaction()
            try {
                val values = ContentValues()
                values.put(Db.PlayerTable.KEY_PLAYER_LEVEL, 1)
                values.put(Db.PlayerTable.KEY_PLAYER_STRENGTH, 1)
                briteDb.update(Db.PlayerTable.TABLE_NAME, values, null)
                transaction.markSuccessful()
                subscriber.onCompleted()
            } finally {
                transaction.end()
            }
        }
    }

    fun clearGameSteps(): Observable<Void> {
        return Observable.create { }
    }

    val gameSteps: Observable<GameStep>
        get() = Observable.create { subscriber ->
            val QUERY = "SELECT " +
                    Db.GameTable.KEY_GAME_PLAYER_ID + ", " +
                    Db.GameTable.KEY_GAME_PLAYER_LEVEL + ", " +
                    Db.GameTable.KEY_GAME_PLAYER_STRENGTH + ", " +
                    Db.PlayerTable.KEY_PLAYER_NAME + ", " +
                    Db.PlayerTable.KEY_PLAYER_COLOR + " FROM " + Db.GameTable.TABLE_NAME +
                    " INNER JOIN " + Db.PlayerTable.TABLE_NAME + " ON players.id = " + Db.GameTable.KEY_GAME_PLAYER_ID
            val cursor = briteDb.query(QUERY)
            while (cursor.moveToNext()) {
                subscriber.onNext(Db.GameTable.parseCursor(cursor))
            }
            cursor.close()
            subscriber.onCompleted()
        }

}
