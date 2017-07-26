package com.datarockets.mnchkn.data.mapper

import com.datarockets.mnchkn.data.entity.PlayerEntity
import com.datarockets.mnchkn.data.models.Player

object PlayerEntityMapper {

    fun transform(player: Player): PlayerEntity = PlayerEntity(
            player.id,
            player.name ?: "Username${System.currentTimeMillis()}",
            player.levelScore,
            player.strengthScore,
            player.color ?: "#FAFAFA",
            player.position,
            player.playing
    )

    fun transform(playerList: List<Player>): List<PlayerEntity> = playerList.map { transform(it) }
}