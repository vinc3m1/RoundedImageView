package com.makeramen;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.ImageView;
import com.squareup.picasso.Transformation;

public final class RoundedTransformationBuilder {

  //private final Resources mResources;
  private final DisplayMetrics mDisplayMetrics;

  private float mCornerRadius = 0;
  private boolean mOval = false;
  private float mBorderWidth = 0;
  private ColorStateList mBorderColor =
      ColorStateList.valueOf(RoundedDrawable.DEFAULT_BORDER_COLOR);
  private ImageView.ScaleType mScaleType = ImageView.ScaleType.FIT_CENTER;

  public RoundedTransformationBuilder() {
    mDisplayMetrics = Resources.getSystem().getDisplayMetrics();
  }

  public RoundedTransformationBuilder scaleType(ImageView.ScaleType scaleType) {
    mScaleType = scaleType;
    return this;
  }

  /**
   * set corner radius in px
   */
  public RoundedTransformationBuilder cornerRadius(float radiusPx) {
    mCornerRadius = radiusPx;
    return this;
  }

  /**
   * set corner radius in dip
   */
  public RoundedTransformationBuilder cornerRadiusDp(float radiusDp) {
    mCornerRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, radiusDp, mDisplayMetrics);
    return this;
  }

  /**
   * set border width in px
   */
  public RoundedTransformationBuilder borderWidth(float widthPx) {
    mBorderWidth = widthPx;
    return this;
  }

  /**
   * set border width in dip
   */
  public RoundedTransformationBuilder borderWidthDp(float widthDp) {
    mBorderWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, widthDp, mDisplayMetrics);
    return this;
  }


  /**
   * set border color
   */
  public RoundedTransformationBuilder borderColor(int color) {
    mBorderColor = ColorStateList.valueOf(color);
    return this;
  }

  public RoundedTransformationBuilder borderColor(ColorStateList colors) {
    mBorderColor = colors;
    return this;
  }

  public RoundedTransformationBuilder oval(boolean oval) {
    mOval = oval;
    return this;
  }

  public Transformation build() {
    return new Transformation() {
      @Override public Bitmap transform(Bitmap source) {
        Bitmap transformed = RoundedDrawable.fromBitmap(source)
            .setScaleType(mScaleType)
            .setCornerRadius(mCornerRadius)
            .setBorderWidth(mBorderWidth)
            .setBorderColor(mBorderColor)
            .setOval(mOval)
            .toBitmap();
        if (!source.equals(transformed)) {
          source.recycle();
        }
        return transformed;
      }

      @Override public String key() {
        return "r:" + mCornerRadius
            + "b:" + mBorderWidth
            + "c:" + mBorderColor
            + "o:" + mOval;
      }
    };
  }
}
