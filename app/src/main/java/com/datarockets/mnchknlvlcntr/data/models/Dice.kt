package com.datarockets.mnchknlvlcntr.data.models

import java.util.*
import javax.inject.Inject

class Dice
@Inject
constructor() {
    private val random: Random = Random()
    private val faces: Int = 6

    fun roll(): String {
        val rollResult = 1 + random.nextInt(faces)
        return rollResult.toString()
    }
}
