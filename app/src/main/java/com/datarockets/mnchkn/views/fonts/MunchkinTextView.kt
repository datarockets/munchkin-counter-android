package com.datarockets.mnchkn.views.fonts

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet

class MunchkinTextView : AppCompatTextView {

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(context)
    }

    private fun init(context: Context) {
        val munchkinFont = FontCache.getTypeface(FONT_NAME, context)
        typeface = munchkinFont
    }

    companion object {
        val FONT_NAME = "buccaner.ttf"
    }

}
