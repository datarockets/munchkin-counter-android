package com.datarockets.mnchkn.data.mapper

import com.datarockets.mnchkn.data.entity.PlayerEntity
import com.datarockets.mnchkn.data.models.Player

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