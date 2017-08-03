package com.datarockets.mnchknlvlcntr

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import com.datarockets.mnchknlvlcntr.data.local.GameDao
import com.datarockets.mnchknlvlcntr.data.local.MunchkinDatabase
import com.datarockets.mnchknlvlcntr.data.local.PlayerDao
import org.junit.After
import org.junit.Before
import java.io.IOException

abstract class MunchkinDatabaseTest {

    lateinit var database: MunchkinDatabase
    lateinit var playerDao: PlayerDao
    lateinit var gameDao: GameDao

    @Before
    fun createDatabase() {
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getTargetContext(), MunchkinDatabase::class.java).build()
        playerDao = database.playerDao()
        gameDao = database.gameDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDatabase() {
        database.close()
    }
}