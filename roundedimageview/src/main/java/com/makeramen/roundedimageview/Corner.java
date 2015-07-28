package com.makeramen.roundedimageview;

import android.support.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
    RoundedDrawable.CORNER_TOP_LEFT, RoundedDrawable.CORNER_TOP_RIGHT,
    RoundedDrawable.CORNER_BOTTOM_LEFT, RoundedDrawable.CORNER_BOTTOM_RIGHT
})
public @interface Corner {}
