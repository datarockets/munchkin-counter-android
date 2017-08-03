package com.datarockets.mnchknlvlcntr

import android.support.test.runner.AndroidJUnit4
import com.datarockets.mnchknlvlcntr.data.mapper.PlayerEntityMapper
import com.datarockets.mnchknlvlcntr.data.mapper.PlayerMapper
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlayerDaoTest : MunchkinDatabaseTest() {

    private val mockCount = 10

    @Test
    @Throws(Exception::class)
    fun getPlayerTest() {
        val playerIn = MockModelFabric.newPlayer(0)

        playerDao.setPlayer(PlayerEntityMapper.transform(playerIn))
        val playerOut = PlayerMapper.transform(playerDao.getPlayerById(playerIn.id))

        assertEquals(playerIn, playerOut)
    }

    @Test
    @Throws(Exception::class)
    fun getPlayerListTest() {
        val playerListIn = MockModelFabric.newPlayerList(mockCount)

        PlayerEntityMapper.transform(playerListIn).forEach { playerDao.setPlayer(it) }
        val playerListOut = PlayerMapper.transform(playerDao.getPlayers())

        assertArrayEquals(playerListIn.toTypedArray(), playerListOut.toTypedArray())
    }

    @Test
    @Throws(Exception::class)
    fun getPlayingPlayersByPositionTest() {
        val playerListIn = MockModelFabric.newPlayerList(mockCount)

        PlayerEntityMapper.transform(playerListIn).forEach { playerDao.setPlayer(it) }
        val playerListOut = PlayerMapper.transform(playerDao.getPlayingPlayersByPosition())

        assertArrayEquals(playerListIn.filter { it.playing }.map { it.position }.sortedDescending().toTypedArray(),
                playerListOut.map { it.position }.toTypedArray())
    }

    @Test
    @Throws(Exception::class)
    fun getPlayingPlayersByLevel() {
        val playerListIn = MockModelFabric.newPlayerList(mockCount)

        PlayerEntityMapper.transform(playerListIn).forEach { playerDao.setPlayer(it) }
        val playerListOut = PlayerMapper.transform(playerDao.getPlayingPlayersByLevel())

        assertArrayEquals(playerListIn.filter { it.playing }.map { it.levelScore }.sortedDescending().toTypedArray(),
                playerListOut.map { it.levelScore }.toTypedArray())
    }

    @Test
    @Throws(Exception::class)
    fun getPlayingPlayersByStrength() {
        val playerListIn = MockModelFabric.newPlayerList(mockCount)

        PlayerEntityMapper.transform(playerListIn).forEach { playerDao.setPlayer(it) }
        val playerListOut = PlayerMapper.transform(playerDao.getPlayingPlayersByStrength())

        assertArrayEquals(playerListIn.filter { it.playing }.map { it.strengthScore }.sortedDescending().toTypedArray(),
                playerListOut.map { it.strengthScore }.toTypedArray())
    }

    @Test
    @Throws(Exception::class)
    fun getPlayingPlayersByTotal() {
        val playerListIn = MockModelFabric.newPlayerList(mockCount)

        PlayerEntityMapper.transform(playerListIn).forEach { playerDao.setPlayer(it) }
        val playerListOut = PlayerMapper.transform(playerDao.getPlayingPlayersByTotal())

        assertArrayEquals(playerListIn.filter { it.playing }.map { it.totalScore }.sortedDescending().toTypedArray(),
                playerListOut.map { it.totalScore }.toTypedArray())
    }

    @Test
    @Throws(Exception::class)
    fun deletePlayerTest() {
        val playerListIn = MockModelFabric.newPlayerList(mockCount)
        val playerToDelete = playerListIn[0]

        PlayerEntityMapper.transform(playerListIn).forEach { playerDao.setPlayer(it) }
        playerDao.deletePlayerById(playerToDelete.id)
        val playerListOut = PlayerMapper.transform(playerDao.getPlayers())

        assertTrue(playerListIn.contains(playerToDelete))
        assertFalse(playerListOut.contains(playerToDelete))
    }

    @Test
    @Throws(Exception::class)
    fun updatePlayerTest() {
        val playerIn = MockModelFabric.newPlayer(0)
        val playerNew = MockModelFabric.newPlayer(1)

        playerDao.setPlayer(PlayerEntityMapper.transform(playerIn))
        playerDao.updatePlayer(PlayerEntityMapper.transform(playerIn.copy(name = playerNew.name, levelScore = playerNew.levelScore,
                strengthScore = playerNew.strengthScore, color = playerNew.color, position = playerNew.position,
                playing = playerNew.playing)))
        val playerOut = PlayerMapper.transform(playerDao.getPlayerById(playerIn.id))

        assertEquals(playerNew.name, playerOut.name)
        assertEquals(playerNew.levelScore, playerOut.levelScore)
        assertEquals(playerNew.strengthScore, playerOut.strengthScore)
        assertEquals(playerNew.totalScore, playerOut.totalScore)
        assertEquals(playerNew.color, playerOut.color)
        assertEquals(playerNew.position, playerOut.position)
        assertEquals(playerNew.playing, playerOut.playing)
    }
}