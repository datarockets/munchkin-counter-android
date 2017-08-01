package com.datarockets.mnchknlvlcntr.utils

import com.amulyakhare.textdrawable.util.ColorGenerator

object ColorUtil {

    fun generatePlayerAvatarColor(): String {
        val generator = ColorGenerator.MATERIAL
        val generatedColor = generator.randomColor
        return "#" + Integer.toHexString(generatedColor).substring(2)
    }
}
