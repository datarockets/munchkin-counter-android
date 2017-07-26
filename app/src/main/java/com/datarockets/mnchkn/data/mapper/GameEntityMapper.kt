package com.datarockets.mnchkn.data.mapper

import com.datarockets.mnchkn.data.entity.GameEntity
import com.datarockets.mnchkn.data.models.GameStep

object GameEntityMapper {

    fun transform(gameStep: GameStep): GameEntity = GameEntity(
            gameStep.playerId,
            gameStep.playerLevel,
            gameStep.playerStrength
    )

    fun transform(gameStepList: List<GameStep>): List<GameEntity> = gameStepList.map { transform(it) }
}