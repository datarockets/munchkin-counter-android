package com.datarockets.mnchkn.data.models

import java.util.*
import javax.inject.Inject

class Dice
@Inject
constructor() {

    private val mRandom: Random = Random()
    private val mFaces: Int = 6

    fun roll(): String {
        val rollResult = 1 + mRandom.nextInt(mFaces)
        return rollResult.toString()
    }

}
