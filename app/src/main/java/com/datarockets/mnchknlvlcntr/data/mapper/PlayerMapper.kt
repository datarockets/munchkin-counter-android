package com.datarockets.mnchknlvlcntr.data.mapper

import com.datarockets.mnchknlvlcntr.data.entity.PlayerEntity
import com.datarockets.mnchknlvlcntr.data.models.Player

object PlayerMapper {

    fun transform(playerEntity: PlayerEntity): Player = Player(
            playerEntity.id,
            playerEntity.name,
            playerEntity.level,
            playerEntity.strength,
            playerEntity.level + playerEntity.strength,
            playerEntity.color,
            playerEntity.position,
            playerEntity.isPlaying
    )

    fun transform(playerEntityList: List<PlayerEntity>): List<Player> = playerEntityList.map { transform(it) }
}