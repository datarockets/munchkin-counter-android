package com.datarockets.mnchkn.data.mapper

import com.datarockets.mnchkn.data.entity.GameEntity
import com.datarockets.mnchkn.data.models.GameStep

object GameStepMapper {

    fun transform(gameEntity: GameEntity): GameStep = GameStep(
            gameEntity.playerId,
            gameEntity.playerLevel,
            gameEntity.playerStrength
    )

    fun transform(gameEntityList: List<GameEntity>): List<GameStep> = gameEntityList.map { transform(it) }
}