package com.datarockets.mnchknlvlcntr

import android.support.test.runner.AndroidJUnit4
import com.datarockets.mnchknlvlcntr.data.mapper.GameEntityMapper
import com.datarockets.mnchknlvlcntr.data.mapper.GameStepMapper
import com.datarockets.mnchknlvlcntr.data.mapper.PlayerEntityMapper
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GameDaoTest : MunchkinDatabaseTest() {

    private val mockPlayerCount = 3
    private val mockGameStepsCount = 10

    @Test
    @Throws(Exception::class)
    fun getGameStepsTest() {
        val playerListIn = MockModelFabric.newPlayerList(mockPlayerCount)
        val gameStepListIn = MockModelFabric.newGameStepList(playerListIn, mockGameStepsCount)

        PlayerEntityMapper.transform(playerListIn).forEach { playerDao.setPlayer(it) }
        GameEntityMapper.transform(gameStepListIn).forEach { gameDao.setGameStep(it) }
        val gameStepListOut = GameStepMapper.transform(gameDao.getGameSteps())

        assertArrayEquals(gameStepListIn.toTypedArray(), gameStepListOut.toTypedArray())
    }

    @Test
    @Throws(Exception::class)
    fun deleteGameStepsTest() {
        val playerListIn = MockModelFabric.newPlayerList(mockPlayerCount)
        val gameStepListIn = MockModelFabric.newGameStepList(playerListIn, mockGameStepsCount)

        PlayerEntityMapper.transform(playerListIn).forEach { playerDao.setPlayer(it) }
        GameEntityMapper.transform(gameStepListIn).forEach { gameDao.setGameStep(it) }
        val gameStepListOutSize = gameDao.getGameSteps().size

        gameDao.deleteGameSteps()

        val gameStepListOutAfterDeleteSize = gameDao.getGameSteps().size

        assertEquals(mockPlayerCount * mockGameStepsCount, gameStepListOutSize)
        assertEquals(0, gameStepListOutAfterDeleteSize)
    }
}