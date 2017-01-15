package com.datarockets.mnchkn.data.local

import android.content.ContentValues
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

    val players: Observable<Player>
        get() = getPlayers(Db.PlayerTable.ORDER_BY_ID)

    fun getPlayers(orderValue: Int): Observable<Player> {
        return Observable.create { subscriber ->
            var orderByQuery: String? = null
            when (orderValue) {
                Db.PlayerTable.ORDER_BY_ID -> orderByQuery = Db.PlayerTable.ORDER_BY + Db.PlayerTable.KEY_PLAYER_ID
                Db.PlayerTable.ORDER_BY_LEVEL -> orderByQuery = Db.PlayerTable.ORDER_BY + Db.PlayerTable.KEY_PLAYER_LEVEL
                Db.PlayerTable.ORDER_BY_STRENGTH -> orderByQuery = Db.PlayerTable.ORDER_BY + Db.PlayerTable.KEY_PLAYER_STRENGTH
                Db.PlayerTable.ORDER_BY_TOTAL -> orderByQuery = Db.PlayerTable.ORDER_BY + Db.PlayerTable.KEY_PLAYER_TOTAL
            }
            val playersTotalQuery = "SELECT " +
            Db.PlayerTable.KEY_PLAYER_ID + ", " +
            Db.PlayerTable.KEY_PLAYER_NAME + ", " +
            Db.PlayerTable.KEY_PLAYER_LEVEL + ", " +
            Db.PlayerTable.KEY_PLAYER_STRENGTH + ", " +
            Db.PlayerTable.KEY_PLAYER_COLOR + ", (" +
            Db.PlayerTable.KEY_PLAYER_LEVEL + " + " + Db.PlayerTable.KEY_PLAYER_STRENGTH + ") AS " + Db.PlayerTable.KEY_PLAYER_TOTAL +
            " FROM " + Db.PlayerTable.TABLE_NAME + orderByQuery + " DESC"
            val cursor = briteDb.query(playersTotalQuery)
            while (cursor.moveToNext()) {
                subscriber.onNext(Db.PlayerTable.parseCursor(cursor))
            }
            cursor.close()
            subscriber.onCompleted()
        }
    }

    fun deletePlayer(playerId: Long): Observable<Void> {
        return Observable.create { subscriber ->
            val transaction = briteDb.newTransaction()
            try {
                briteDb.delete(Db.PlayerTable.TABLE_NAME, Db.PlayerTable.KEY_PLAYER_ID + " = ?",
                        *arrayOf(java.lang.Long.toString(playerId)))
                transaction.markSuccessful()
                subscriber.onCompleted()
            } finally {
                transaction.end()
            }
        }
    }

    fun updatePlayer(player: Player): Observable<Player> {
        return Observable.create { subscriber ->
            val transaction = briteDb.newTransaction()
            try {
                val contentValues = Db.PlayerTable.toContentValues(player)
                briteDb.update(Db.PlayerTable.TABLE_NAME,
                        contentValues,
                        Db.PlayerTable.KEY_PLAYER_ID + " = ?", player.id.toString())
                transaction.markSuccessful()
                subscriber.onNext(player)
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
