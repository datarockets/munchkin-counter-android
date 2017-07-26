package com.datarockets.mnchkn.data.utils;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;


import static com.datarockets.mnchkn.data.utils.SortType.LEVEL;
import static com.datarockets.mnchkn.data.utils.SortType.NONE;
import static com.datarockets.mnchkn.data.utils.SortType.POSITION;
import static com.datarockets.mnchkn.data.utils.SortType.STRENGTH;
import static com.datarockets.mnchkn.data.utils.SortType.TOTAL;
import static java.lang.annotation.RetentionPolicy.SOURCE;

@Retention(SOURCE)
@IntDef({NONE, POSITION, LEVEL, STRENGTH, TOTAL})
public @interface SortType {
    int NONE = -1;
    int POSITION = 0;
    int LEVEL = 1;
    int STRENGTH = 2;
    int TOTAL = 3;
}
