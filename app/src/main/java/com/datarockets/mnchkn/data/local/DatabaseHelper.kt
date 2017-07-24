package com.datarockets.mnchkn.data.local

import android.content.ContentValues
import com.datarockets.mnchkn.data.models.GameStep
import com.datarockets.mnchkn.data.models.Player
import com.squareup.sqlbrite2.BriteDatabase
import com.squareup.sqlbrite2.SqlBrite
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
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
        briteDb = briteBuilder.build().wrapDatabaseHelper(dbOpenHelper, Schedulers.trampoline())
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
                subscriber.onComplete()
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
            subscriber.onComplete()
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
            subscriber.onComplete()
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
            subscriber.onComplete()
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
            subscriber.onComplete()
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
            subscriber.onComplete()
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
            subscriber.onComplete()
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
            subscriber.onComplete()
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
                subscriber.onComplete()
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
                subscriber.onComplete()
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
                subscriber.onComplete()
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
                subscriber.onComplete()
            } finally {
                transaction.end()
            }
        }
    }

    fun changePlayersPositions(movedPlayerId: Long,
                               newPosition: Int): Observable<Void> {
        return Observable.create { subscriber ->
            val transaction = briteDb.newTransaction()
            try {
                val contentValues = ContentValues()
                contentValues.put(Db.PlayerTable.KEY_PLAYER_POSITION, newPosition)
                briteDb.update(Db.PlayerTable.TABLE_NAME,
                        contentValues,
                        Db.PlayerTable.KEY_PLAYER_ID + " = ?", movedPlayerId.toString())
                transaction.markSuccessful()
                subscriber.onComplete()
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
                subscriber.onComplete()
            } finally {
                transaction.end()
            }
        }
    }

    fun clearGameSteps(): Observable<Void> {
        return Observable.create { subscriber ->
            val transaction = briteDb.newTransaction()
            try {
                val values = ContentValues()
                values.put(Db.PlayerTable.KEY_PLAYER_LEVEL, 1)
                values.put(Db.PlayerTable.KEY_PLAYER_STRENGTH, 1)
                briteDb.update(Db.PlayerTable.TABLE_NAME, values, null)
                briteDb.execute("DELETE FROM " + Db.GameTable.TABLE_NAME)
                transaction.markSuccessful()
                subscriber.onComplete()
            } finally {
                transaction.end()
            }
        }
    }

    fun getGameSteps(): Observable<GameStep> {
        return Observable.create { subscriber ->
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
            subscriber.onComplete()
        }
    }
}
