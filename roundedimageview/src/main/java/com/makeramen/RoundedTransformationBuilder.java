package com.makeramen;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.ImageView;
import com.squareup.picasso.Transformation;

public final class RoundedTransformationBuilder {

  private final Resources mResources;
  private final DisplayMetrics mDisplayMetrics;

  private float mCornerRadius = 0;
  private boolean mOval = false;
  private float mBorderWidth = 0;
  private ColorStateList mBorderColor =
      ColorStateList.valueOf(RoundedDrawable.DEFAULT_BORDER_COLOR);
  private ImageView.ScaleType mScaleType = ImageView.ScaleType.FIT_CENTER;

  public RoundedTransformationBuilder(Context context) {
    mResources = context.getResources();
    mDisplayMetrics = mResources.getDisplayMetrics();
  }

  public RoundedTransformationBuilder scaleType(ImageView.ScaleType scaleType) {
    mScaleType = scaleType;
    return this;
  }

  public RoundedTransformationBuilder cornerRadiusPx(int radiusPx) {
    mCornerRadius = radiusPx;
    return this;
  }

  public RoundedTransformationBuilder cornerRadiusDp(int radiusDp) {
    mCornerRadius =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, radiusDp, mDisplayMetrics);
    return this;
  }

  public RoundedTransformationBuilder cornerRadiusDimen(int radiusResId) {
    mCornerRadius = mResources.getDimension(radiusResId);
    return this;
  }

  public RoundedTransformationBuilder borderWidthDimen(int widthResId) {
    mBorderWidth = mResources.getDimension(widthResId);
    return this;
  }

  public RoundedTransformationBuilder borderWidthPx(float widthPx) {
    mBorderWidth = widthPx;
    return this;
  }

  public RoundedTransformationBuilder borderWidthDp(float widthDp) {
    mBorderWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, widthDp, mDisplayMetrics);
    return this;
  }

  public RoundedTransformationBuilder borderColorResource(int colorResId) {
    mBorderColor = ColorStateList.valueOf(mResources.getColor(colorResId));
    return this;
  }

  public RoundedTransformationBuilder borderColor(int color) {
    mBorderColor = ColorStateList.valueOf(color);
    return this;
  }

  public RoundedTransformationBuilder borderColorsResource(int colorsResId) {
    mBorderColor = mResources.getColorStateList(colorsResId);
    return this;
  }

  public RoundedTransformationBuilder borderColors(ColorStateList colors) {
    mBorderColor = colors;
    return this;
  }

  public RoundedTransformationBuilder ovalResource(int ovalResId) {
    mOval = mResources.getBoolean(ovalResId);
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
            .setBorderColors(mBorderColor)
            .setOval(mOval)
            .toBitmap();
        if (!source.equals(transformed)) {
          source.recycle();
        }
        return transformed;
      }

      @Override public String key() {
        return "rounded_radius_"
            + mCornerRadius
            + "_border_"
            + mBorderWidth
            + "_color_"
            + mBorderColor
            + "_oval_"
            + mOval;
      }
    };
  }
}
