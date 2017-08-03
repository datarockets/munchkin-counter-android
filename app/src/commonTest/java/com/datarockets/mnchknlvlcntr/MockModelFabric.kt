package com.datarockets.mnchknlvlcntr

import com.amulyakhare.textdrawable.util.ColorGenerator
import com.datarockets.mnchknlvlcntr.data.models.GameStep
import com.datarockets.mnchknlvlcntr.data.models.Player
import java.util.*

object MockModelFabric {

    fun newPlayer(position: Int): Player {
        val random = Random()
        val level = random.nextInt(100)
        val strength = random.nextInt(100)
        return Player(System.nanoTime(), randomString(), level, strength, level + strength, newPlayerColor(),
                position, random.nextBoolean())
    }

    fun newPlayerList(count: Int): List<Player> = (0..count - 1).map { newPlayer(it) }

    fun newGameStep(playerId: Long): GameStep {
        val random = Random()
        return GameStep(playerId, random.nextInt(), random.nextInt())
    }

    fun newGameStepList(playerList: List<Player>, count: Int): List<GameStep> {
        val gameStepList = mutableListOf<GameStep>()
        playerList.forEach { (id) -> (0..count - 1).forEach { gameStepList.add(newGameStep(id)) } }
        return gameStepList
    }

    private fun randomString(): String = UUID.randomUUID().toString()

    private fun newPlayerColor(): String = Integer.toHexString(ColorGenerator.MATERIAL.randomColor).substring(2)
}
