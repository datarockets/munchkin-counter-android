package com.datarockets.mnchknlvlcntr

import com.amulyakhare.textdrawable.util.ColorGenerator
import com.datarockets.mnchknlvlcntr.data.models.GameStep
import com.datarockets.mnchknlvlcntr.data.models.Player
import java.util.*

object MockModelFabric {

    fun randomString(): String {
        return UUID.randomUUID().toString()
    }

    fun newPlayer(): Player {
        val random = Random()
        val player = Player()
        player.id = random.nextLong()
        player.name = newPlayerName()
        player.color = newPlayerColor()
        return player
    }

    fun newPlayer(position: Int): Player {
        val player = newPlayer()
        player.position = position
        return player
    }

    fun newStep(): GameStep {
        val gameStep = GameStep()
        gameStep.playerId = 1
        gameStep.playerLevel = 1
        gameStep.playerLevel = 1
        return gameStep
    }


    fun newPlayersList(size: Int): List<Player> {
        val list: MutableList<Player> = mutableListOf()
        (0..size - 1).mapTo(list) { newPlayer(it) }
        return list
    }

    fun newPlayerName(): String {
        return randomString()
    }

    fun newPlayerColor(): String {
        val generatedColor = ColorGenerator.MATERIAL.randomColor
        return Integer.toHexString(generatedColor).substring(2)
    }

}
