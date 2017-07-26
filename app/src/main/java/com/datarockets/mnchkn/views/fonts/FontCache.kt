package com.datarockets.mnchkn.views.fonts

import android.content.Context
import android.graphics.Typeface
import timber.log.Timber
import java.util.*

object FontCache {

    private val fontNameTypefaceMap = HashMap<String, Typeface>()

    fun getTypeface(fontName: String, context: Context): Typeface {
        var typeface: Typeface? = fontNameTypefaceMap[fontName]
        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(context.assets, fontName)
            } catch (e: Exception) {
                Timber.e("Error while trying to get typeface")
            }
            fontNameTypefaceMap.put(fontName, typeface!!)
        }
        return typeface
    }
}
