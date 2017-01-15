package com.datarockets.mnchkn.views.fonts;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;

import timber.log.Timber;

public class FontCache {

    private static HashMap<String, Typeface> fontNameTypefaceMap = new HashMap<>();

    public static Typeface getTypeface(String fontName, Context context) {
        Typeface typeface = fontNameTypefaceMap.get(fontName);
        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(context.getAssets(), fontName);
            } catch (Exception e) {
                Timber.e("Error while trying to get typeface");
            }
            fontNameTypefaceMap.put(fontName, typeface);
        }
        return typeface;
    }

}
