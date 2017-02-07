package com.datarockets.mnchkn.store;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.datarockets.mnchkn.data.models.GameStep;
import com.datarockets.mnchkn.data.models.Player;

import java.util.ArrayList;

import javax.inject.Singleton;

@Singleton
public class MunchkinDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "database";

    private static final String DATABASE_NAME = "players_db";
    private static final int DATABASE_VERSION = 6;

    private static final String TABLE_PLAYERS = "players";

    private static final String KEY_PLAYER_ID = "id";
    private static final String KEY_PLAYER_NAME = "name";
    private static final String KEY_PLAYER_LEVEL = "level";
    private static final String KEY_PLAYER_STRENGTH = "strength";
    private static final String KEY_PLAYER_COLOR = "color";
    private static final String KEY_PLAYER_TOTAL = "total";

    private static final String TABLE_GAME = "game";

    private static final String KEY_GAME_PLAYER_ID = "player_id";
    private static final String KEY_GAME_PLAYER_LEVEL = "player_level";
    private static final String KEY_GAME_PLAYER_STRENGTH = "player_strength";

    private static final String ORDER_BY = " ORDER BY ";

    private static final int ORDER_BY_ID = 0;
    private static final int ORDER_BY_LEVEL = 1;
    private static final int ORDER_BY_STRENGTH = 2;
    private static final int ORDER_BY_TOTAL = 3;

    public MunchkinDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createPlayersTableQuery = "CREATE TABLE " + TABLE_PLAYERS +
                "(" +
                KEY_PLAYER_ID + " INTEGER PRIMARY KEY," +
                KEY_PLAYER_NAME + " TEXT," +
                KEY_PLAYER_LEVEL + " INTEGER," +
                KEY_PLAYER_STRENGTH + " INTEGER," +
                KEY_PLAYER_COLOR + " TEXT" +
                ")";
        String createGameTableQuery = "CREATE TABLE " + TABLE_GAME +
                "(" +
                KEY_GAME_PLAYER_ID + " INTEGER REFERENCES " + TABLE_PLAYERS + "," +
                KEY_GAME_PLAYER_LEVEL + " INTEGER," +
                KEY_GAME_PLAYER_STRENGTH + " INTEGER" +
                ")";
        db.execSQL(createPlayersTableQuery);
        db.execSQL(createGameTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                db.execSQL("ALTER TABLE "
                        + TABLE_PLAYERS + " ADD COLUMN "
                        + KEY_PLAYER_COLOR + " TEXT");
                if (!isTableEmpty(db, TABLE_PLAYERS)) {
                    addColorsToUpdatedPlayers(db);
                }
            default:
                break;
        }
    }

    public long addPlayer(Player player) {
        long playerId = -1;
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_PLAYER_NAME, player.getName());
            values.put(KEY_PLAYER_LEVEL, player.getLevelScore());
            values.put(KEY_PLAYER_STRENGTH, player.getStrengthScore());
            values.put(KEY_PLAYER_COLOR, player.getColor());
            playerId = db.insertOrThrow(TABLE_PLAYERS, null, values);
            db.setTransactionSuccessful();
            Log.i(TAG, "Player id is " + playerId);
        } catch (Exception e) {
            Log.e(TAG, "Error while trying to add new player");
        } finally {
            db.endTransaction();
        }
        return playerId;
    }

    public ArrayList<Player> getPlayers() {
        return getPlayers(ORDER_BY_ID);
    }

    public ArrayList<Player> getPlayers(int orderValue) {
        ArrayList<Player> playersList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String orderByQuery = null;
        switch (orderValue) {
            case ORDER_BY_ID:
                orderByQuery = ORDER_BY + KEY_PLAYER_ID;
                break;
            case ORDER_BY_LEVEL:
                orderByQuery = ORDER_BY + KEY_PLAYER_LEVEL;
                break;
            case ORDER_BY_STRENGTH:
                orderByQuery = ORDER_BY + KEY_PLAYER_STRENGTH;
                break;
            case ORDER_BY_TOTAL:
                orderByQuery = ORDER_BY + KEY_PLAYER_TOTAL;
                break;
        }
        String playersTotalQuery = "SELECT "
                + KEY_PLAYER_ID + ", "
                + KEY_PLAYER_NAME + ", "
                + KEY_PLAYER_LEVEL + ", "
                + KEY_PLAYER_STRENGTH + ", "
                + KEY_PLAYER_COLOR + ", ("
                + KEY_PLAYER_LEVEL + " + " + KEY_PLAYER_STRENGTH + ") AS " + KEY_PLAYER_TOTAL
                + " FROM " + TABLE_PLAYERS + orderByQuery + " DESC";
        Log.v(TAG, playersTotalQuery);
        Cursor cursor = db.rawQuery(playersTotalQuery, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Player player = new Player();
                    player.setId(cursor.getLong(cursor.
                            getColumnIndex(KEY_PLAYER_ID)));
                    player.setName(cursor.getString(cursor.
                            getColumnIndex(KEY_PLAYER_NAME)));
                    player.setColor(cursor.getString(cursor.
                            getColumnIndex(KEY_PLAYER_COLOR)));
                    player.setLevelScore(cursor.getInt(cursor.
                            getColumnIndex(KEY_PLAYER_LEVEL)));
                    player.setStrengthScore(cursor.getInt(cursor.
                            getColumnIndex(KEY_PLAYER_STRENGTH)));
                    player.setTotalScore(cursor.getInt(cursor.
                            getColumnIndex(KEY_PLAYER_TOTAL)));
                    playersList.add(player);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while getting players");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return playersList;
    }

    public Player updatePlayer(Player player) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PLAYER_LEVEL, player.getLevelScore());
        values.put(KEY_PLAYER_STRENGTH, player.getStrengthScore());
        db.update(TABLE_PLAYERS, values, KEY_PLAYER_ID + " = ?",
                new String[] {String.valueOf(player.getId())});
        return player;
    }

    public void deletePlayer(long id) {
        Log.v(TAG, "Id of the player is " + id);
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_PLAYERS, KEY_PLAYER_ID + " = ?", new String[] {Long.toString(id)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Error while deleting player");
        } finally {
            db.endTransaction();
        }
    }

    private boolean isTableEmpty(SQLiteDatabase db, String tableName) {
        String countQuery = "SELECT COUNT(*) FROM " + tableName;
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        if (count > 0) {
            return false;
        } else {
            return true;
        }
    }

    private void addColorsToUpdatedPlayers(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(KEY_PLAYER_COLOR, "#3B1606");
        db.update(TABLE_PLAYERS, values, null, null);
    }

    public void clearPlayersStats() {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PLAYER_LEVEL, 1);
        values.put(KEY_PLAYER_STRENGTH, 1);
        db.update(TABLE_PLAYERS, values, null, null);
    }

    public void insertStep(GameStep gameStep) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_GAME_PLAYER_ID, gameStep.getPlayerId());
            values.put(KEY_GAME_PLAYER_LEVEL, gameStep.getPlayerLevel());
            values.put(KEY_GAME_PLAYER_STRENGTH, gameStep.getPlayerStrength());
            db.insertOrThrow(TABLE_GAME, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Error while trying to insert step to database");
        } finally {
            db.endTransaction();
        }
    }

    public void clearSteps() {
        Log.v(TAG, "Clearing game steps");
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_GAME, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Error while trying to delete game steps");
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<GameStep> getGameSteps() {
        Log.v(TAG, "GET GAME STEPS");
        ArrayList<GameStep> gameSteps = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String lineChartQuery = "SELECT "
                + KEY_GAME_PLAYER_ID + ", "
                + KEY_GAME_PLAYER_LEVEL + ", "
                + KEY_GAME_PLAYER_STRENGTH + ", "
                + KEY_PLAYER_NAME + ", "
                + KEY_PLAYER_COLOR + " FROM " + TABLE_GAME
                + " INNER JOIN " + TABLE_PLAYERS + " ON players.id = " + KEY_GAME_PLAYER_ID;
        Log.v("TAG", lineChartQuery);
        Cursor cursor = db.rawQuery(lineChartQuery, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    GameStep gameStep = new GameStep();
                    gameStep.setPlayerId(cursor.getLong(cursor.
                            getColumnIndex(KEY_GAME_PLAYER_ID)));
                    gameStep.setPlayerLevel(cursor.getInt(cursor.
                            getColumnIndex(KEY_GAME_PLAYER_LEVEL)));
                    gameStep.setPlayerStrength(cursor.getInt(cursor.
                            getColumnIndex(KEY_GAME_PLAYER_STRENGTH)));
                    gameSteps.add(gameStep);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while getting game steps");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return gameSteps;
    }

}
