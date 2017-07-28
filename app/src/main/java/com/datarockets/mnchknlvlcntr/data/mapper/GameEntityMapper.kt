package com.datarockets.mnchknlvlcntr.data.mapper

import com.datarockets.mnchknlvlcntr.data.entity.GameEntity
import com.datarockets.mnchknlvlcntr.data.models.GameStep

object GameEntityMapper {

    fun transform(gameStep: GameStep): GameEntity = GameEntity(
            gameStep.playerId,
            gameStep.playerLevel,
            gameStep.playerStrength
    )

    fun transform(gameStepList: List<GameStep>): List<GameEntity> = gameStepList.map { transform(it) }
}