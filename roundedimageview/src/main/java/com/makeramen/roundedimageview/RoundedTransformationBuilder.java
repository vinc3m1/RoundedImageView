/*
* Copyright (C) 2015 Vincent Mi
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.makeramen.roundedimageview;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.ImageView;
import com.squareup.picasso.Transformation;
import java.util.Arrays;

import static com.makeramen.roundedimageview.RoundedDrawable.CORNER_BOTTOM_LEFT;
import static com.makeramen.roundedimageview.RoundedDrawable.CORNER_BOTTOM_RIGHT;
import static com.makeramen.roundedimageview.RoundedDrawable.CORNER_TOP_LEFT;
import static com.makeramen.roundedimageview.RoundedDrawable.CORNER_TOP_RIGHT;

public final class RoundedTransformationBuilder {

  //private final Resources mResources;
  private final DisplayMetrics mDisplayMetrics;

  private float[] mCornerRadii = new float[] { 0, 0, 0, 0 };

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
   * Set corner radius for all corners in px.
   *
   * @param radius the radius in px
   * @return the builder for chaining.
   */
  public RoundedTransformationBuilder cornerRadius(float radius) {
    mCornerRadii[CORNER_TOP_LEFT] = radius;
    mCornerRadii[CORNER_TOP_RIGHT] = radius;
    mCornerRadii[CORNER_BOTTOM_RIGHT] = radius;
    mCornerRadii[CORNER_BOTTOM_LEFT] = radius;
    return this;
  }

  /**
   * Set corner radius for a specific corner in px.
   *
   * @param corner the corner to set.
   * @param radius the radius in px.
   * @return the builder for chaning.
   */
  public RoundedTransformationBuilder cornerRadius(int corner, float radius) {
    mCornerRadii[corner] = radius;
    return this;
  }

  /**
   * Set corner radius for all corners in density independent pixels.
   *
   * @param radius the radius in density independent pixels.
   * @return the builder for chaining.
   */
  public RoundedTransformationBuilder cornerRadiusDp(float radius) {
    return cornerRadius(
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, radius, mDisplayMetrics));
  }

  /**
   * Set corner radius for a specific corner in density independent pixels.
   *
   * @param corner the corner to set
   * @param radius the radius in density independent pixels.
   * @return the builder for chaining.
   */
  public RoundedTransformationBuilder cornerRadiusDp(int corner, float radius) {
    return cornerRadius(corner,
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, radius, mDisplayMetrics));
  }

  /**
   * Set the border width in pixels.
   *
   * @param width border width in pixels.
   * @return the builder for chaining.
   */
  public RoundedTransformationBuilder borderWidth(float width) {
    mBorderWidth = width;
    return this;
  }

  /**
   * Set the border width in density independent pixels.
   *
   * @param width border width in density independent pixels.
   * @return the builder for chaining.
   */
  public RoundedTransformationBuilder borderWidthDp(float width) {
    mBorderWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, mDisplayMetrics);
    return this;
  }

  /**
   * Set the border color.
   *
   * @param color the color to set.
   * @return the builder for chaining.
   */
  public RoundedTransformationBuilder borderColor(int color) {
    mBorderColor = ColorStateList.valueOf(color);
    return this;
  }

  /**
   * Set the border color as a {@link ColorStateList}.
   *
   * @param colors the {@link ColorStateList} to set.
   * @return the builder for chaining.
   */
  public RoundedTransformationBuilder borderColor(ColorStateList colors) {
    mBorderColor = colors;
    return this;
  }

  /**
   * Sets whether the image should be oval or not.
   *
   * @param oval if the image should be oval.
   * @return the builder for chaining.
   */
  public RoundedTransformationBuilder oval(boolean oval) {
    mOval = oval;
    return this;
  }

  /**
   * Creates a {@link Transformation} for use with picasso.
   *
   * @return the {@link Transformation}
   */
  public Transformation build() {
    return new Transformation() {
      @Override public Bitmap transform(Bitmap source) {
        Bitmap transformed = RoundedDrawable.fromBitmap(source)
            .setScaleType(mScaleType)
            .setCornerRadius(mCornerRadii[0], mCornerRadii[1], mCornerRadii[2], mCornerRadii[3])
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
        return "r:" + Arrays.toString(mCornerRadii)
            + "b:" + mBorderWidth
            + "c:" + mBorderColor
            + "o:" + mOval;
      }
    };
  }
}
