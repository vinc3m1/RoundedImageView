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

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.ColorFilter;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import static com.makeramen.roundedimageview.RoundedDrawable.CORNER_BOTTOM_LEFT;
import static com.makeramen.roundedimageview.RoundedDrawable.CORNER_BOTTOM_RIGHT;
import static com.makeramen.roundedimageview.RoundedDrawable.CORNER_TOP_LEFT;
import static com.makeramen.roundedimageview.RoundedDrawable.CORNER_TOP_RIGHT;


@SuppressWarnings("UnusedDeclaration")
public class RoundedImageView extends ImageView {

  // Constants for tile mode attributes
  private static final int TILE_MODE_UNDEFINED = -2;
  private static final int TILE_MODE_CLAMP = 0;
  private static final int TILE_MODE_REPEAT = 1;
  private static final int TILE_MODE_MIRROR = 2;

  public static final String TAG = "RoundedImageView";
  public static final float DEFAULT_RADIUS = 0f;
  public static final float DEFAULT_BORDER_WIDTH = 0f;
  public static final Shader.TileMode DEFAULT_TILE_MODE = Shader.TileMode.CLAMP;
  private static final ScaleType[] SCALE_TYPES = {
      ScaleType.MATRIX,
      ScaleType.FIT_XY,
      ScaleType.FIT_START,
      ScaleType.FIT_CENTER,
      ScaleType.FIT_END,
      ScaleType.CENTER,
      ScaleType.CENTER_CROP,
      ScaleType.CENTER_INSIDE
  };

  private final float[] mCornerRadii =
      new float[] { DEFAULT_RADIUS, DEFAULT_RADIUS, DEFAULT_RADIUS, DEFAULT_RADIUS };

  private Drawable mBackgroundDrawable;
  private ColorStateList mBorderColor =
      ColorStateList.valueOf(RoundedDrawable.DEFAULT_BORDER_COLOR);
  private float mBorderWidth = DEFAULT_BORDER_WIDTH;
  private ColorFilter mColorFilter = null;
  private boolean mColorMod = false;
  private Drawable mDrawable;
  private boolean mHasColorFilter = false;
  private boolean mIsOval = false;
  private boolean mMutateBackground = false;
  private int mResource;
  private ScaleType mScaleType = ScaleType.FIT_CENTER;
  private Shader.TileMode mTileModeX = DEFAULT_TILE_MODE;
  private Shader.TileMode mTileModeY = DEFAULT_TILE_MODE;

  public RoundedImageView(Context context) {
    super(context);
  }

  public RoundedImageView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public RoundedImageView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);

    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundedImageView, defStyle, 0);

    int index = a.getInt(R.styleable.RoundedImageView_android_scaleType, -1);
    if (index >= 0) {
      setScaleType(SCALE_TYPES[index]);
    } else {
      // default scaletype to FIT_CENTER
      setScaleType(ScaleType.FIT_CENTER);
    }

    float cornerRadiusOverride =
        a.getDimensionPixelSize(R.styleable.RoundedImageView_riv_corner_radius, -1);

    mCornerRadii[CORNER_TOP_LEFT] = a.getDimensionPixelSize(R.styleable.RoundedImageView_riv_corner_radius_top_left, -1);
    mCornerRadii[CORNER_TOP_RIGHT] = a.getDimensionPixelSize(R.styleable.RoundedImageView_riv_corner_radius_top_right, -1);
    mCornerRadii[CORNER_BOTTOM_RIGHT] = a.getDimensionPixelSize(R.styleable.RoundedImageView_riv_corner_radius_bottom_right, -1);
    mCornerRadii[CORNER_BOTTOM_LEFT] = a.getDimensionPixelSize(R.styleable.RoundedImageView_riv_corner_radius_bottom_left, -1);

    boolean any = false;
    for (int i = 0, len = mCornerRadii.length; i < len; i++) {
      if (mCornerRadii[i] < 0) {
        mCornerRadii[i] = 0f;
      } else {
        any = true;
      }
    }

    if (!any) {
      if (cornerRadiusOverride < 0) {
        cornerRadiusOverride = DEFAULT_RADIUS;
      }
      for (int i = 0, len = mCornerRadii.length; i < len; i++) {
        mCornerRadii[i] = cornerRadiusOverride;
      }
    }

    mBorderWidth = a.getDimensionPixelSize(R.styleable.RoundedImageView_riv_border_width, -1);
    if (mBorderWidth < 0) {
      mBorderWidth = DEFAULT_BORDER_WIDTH;
    }

    mBorderColor = a.getColorStateList(R.styleable.RoundedImageView_riv_border_color);
    if (mBorderColor == null) {
      mBorderColor = ColorStateList.valueOf(RoundedDrawable.DEFAULT_BORDER_COLOR);
    }

    mMutateBackground = a.getBoolean(R.styleable.RoundedImageView_riv_mutate_background, false);
    mIsOval = a.getBoolean(R.styleable.RoundedImageView_riv_oval, false);

    final int tileMode = a.getInt(R.styleable.RoundedImageView_riv_tile_mode, TILE_MODE_UNDEFINED);
    if (tileMode != TILE_MODE_UNDEFINED) {
      setTileModeX(parseTileMode(tileMode));
      setTileModeY(parseTileMode(tileMode));
    }

    final int tileModeX =
        a.getInt(R.styleable.RoundedImageView_riv_tile_mode_x, TILE_MODE_UNDEFINED);
    if (tileModeX != TILE_MODE_UNDEFINED) {
      setTileModeX(parseTileMode(tileModeX));
    }

    final int tileModeY =
        a.getInt(R.styleable.RoundedImageView_riv_tile_mode_y, TILE_MODE_UNDEFINED);
    if (tileModeY != TILE_MODE_UNDEFINED) {
      setTileModeY(parseTileMode(tileModeY));
    }

    updateDrawableAttrs();
    updateBackgroundDrawableAttrs(true);

    a.recycle();
  }

  private static Shader.TileMode parseTileMode(int tileMode) {
    switch (tileMode) {
      case TILE_MODE_CLAMP:
        return Shader.TileMode.CLAMP;
      case TILE_MODE_REPEAT:
        return Shader.TileMode.REPEAT;
      case TILE_MODE_MIRROR:
        return Shader.TileMode.MIRROR;
      default:
        return null;
    }
  }

  @Override
  protected void drawableStateChanged() {
    super.drawableStateChanged();
    invalidate();
  }

  @Override
  public ScaleType getScaleType() {
    return mScaleType;
  }

  @Override
  public void setScaleType(ScaleType scaleType) {
    assert scaleType != null;

    if (mScaleType != scaleType) {
      mScaleType = scaleType;

      switch (scaleType) {
        case CENTER:
        case CENTER_CROP:
        case CENTER_INSIDE:
        case FIT_CENTER:
        case FIT_START:
        case FIT_END:
        case FIT_XY:
          super.setScaleType(ScaleType.FIT_XY);
          break;
        default:
          super.setScaleType(scaleType);
          break;
      }

      updateDrawableAttrs();
      updateBackgroundDrawableAttrs(false);
      invalidate();
    }
  }

  @Override
  public void setImageDrawable(Drawable drawable) {
    mResource = 0;
    mDrawable = RoundedDrawable.fromDrawable(drawable);
    updateDrawableAttrs();
    super.setImageDrawable(mDrawable);
  }

  @Override
  public void setImageBitmap(Bitmap bm) {
    mResource = 0;
    mDrawable = RoundedDrawable.fromBitmap(bm);
    updateDrawableAttrs();
    super.setImageDrawable(mDrawable);
  }

  @Override
  public void setImageResource(@DrawableRes int resId) {
    if (mResource != resId) {
      mResource = resId;
      mDrawable = resolveResource();
      updateDrawableAttrs();
      super.setImageDrawable(mDrawable);
    }
  }

  @Override public void setImageURI(Uri uri) {
    super.setImageURI(uri);
    setImageDrawable(getDrawable());
  }

  private Drawable resolveResource() {
    Resources rsrc = getResources();
    if (rsrc == null) { return null; }

    Drawable d = null;

    if (mResource != 0) {
      try {
        d = rsrc.getDrawable(mResource);
      } catch (Exception e) {
        Log.w(TAG, "Unable to find resource: " + mResource, e);
        // Don't try again.
        mResource = 0;
      }
    }
    return RoundedDrawable.fromDrawable(d);
  }

  @Override
  public void setBackground(Drawable background) {
    setBackgroundDrawable(background);
  }

  private void updateDrawableAttrs() {
    updateAttrs(mDrawable);
  }

  private void updateBackgroundDrawableAttrs(boolean convert) {
    if (mMutateBackground) {
      if (convert) {
        mBackgroundDrawable = RoundedDrawable.fromDrawable(mBackgroundDrawable);
      }
      updateAttrs(mBackgroundDrawable);
    }
  }

  @Override public void setColorFilter(ColorFilter cf) {
    if (mColorFilter != cf) {
      mColorFilter = cf;
      mHasColorFilter = true;
      mColorMod = true;
      applyColorMod();
      invalidate();
    }
  }

  private void applyColorMod() {
    // Only mutate and apply when modifications have occurred. This should
    // not reset the mColorMod flag, since these filters need to be
    // re-applied if the Drawable is changed.
    if (mDrawable != null && mColorMod) {
      mDrawable = mDrawable.mutate();
      if (mHasColorFilter) {
        mDrawable.setColorFilter(mColorFilter);
      }
      // TODO: support, eventually...
      //mDrawable.setXfermode(mXfermode);
      //mDrawable.setAlpha(mAlpha * mViewAlphaScale >> 8);
    }
  }

  private void updateAttrs(Drawable drawable) {
    if (drawable == null) { return; }

    if (drawable instanceof RoundedDrawable) {
      ((RoundedDrawable) drawable)
          .setScaleType(mScaleType)
          .setBorderWidth(mBorderWidth)
          .setBorderColor(mBorderColor)
          .setOval(mIsOval)
          .setTileModeX(mTileModeX)
          .setTileModeY(mTileModeY);

      if (mCornerRadii != null) {
        ((RoundedDrawable) drawable).setCornerRadius(mCornerRadii[CORNER_TOP_LEFT],
            mCornerRadii[CORNER_TOP_RIGHT], mCornerRadii[CORNER_BOTTOM_RIGHT],
            mCornerRadii[CORNER_BOTTOM_LEFT]);
      }

      applyColorMod();
    } else if (drawable instanceof LayerDrawable) {
      // loop through layers to and set drawable attrs
      LayerDrawable ld = ((LayerDrawable) drawable);
      for (int i = 0, layers = ld.getNumberOfLayers(); i < layers; i++) {
        updateAttrs(ld.getDrawable(i));
      }
    }
  }

  @Override
  @Deprecated
  public void setBackgroundDrawable(Drawable background) {
    mBackgroundDrawable = background;
    updateBackgroundDrawableAttrs(true);
    super.setBackgroundDrawable(mBackgroundDrawable);
  }

  /**
   * @return the largest corner radius.
   */
  public float getCornerRadius() {
    return getMaxCornerRadius();
  }

  /**
   * @return the largest corner radius.
   */
  public float getMaxCornerRadius() {
    float maxRadius = 0;
    for (float r : mCornerRadii) {
      maxRadius = Math.max(r, maxRadius);
    }
    return maxRadius;
  }

  /**
   * Get the corner radius of a specified corner.
   *
   * @param corner the corner.
   * @return the radius.
   */
  public float getCornerRadius(int corner) {
    return mCornerRadii[corner];
  }

  /**
   * Set all the corner radii from a dimension resource id.
   *
   * @param resId dimension resource id of radii.
   */
  public void setCornerRadiusDimen(@DimenRes int resId) {
    float radius = getResources().getDimension(resId);
    setCornerRadius(radius, radius, radius, radius);
  }

  /**
   * Set the corner radius of a specific corner from a dimension resource id.
   *
   * @param corner the corner to set.
   * @param resId the dimension resource id of the corner radius.
   */
  public void setCornerRadiusDimen(int corner, @DimenRes int resId) {
    setCornerRadius(corner, getResources().getDimensionPixelSize(resId));
  }

  /**
   * Set the corner radii of all corners in px.
   *
   * @param radius the radius to set.
   */
  public void setCornerRadius(float radius) {
    setCornerRadius(radius, radius, radius, radius);
  }

  /**
   * Set the corner radius of a specific corner in px.
   *
   * @param corner the corner to set.
   * @param radius the corner radius to set in px.
   */
  public void setCornerRadius(int corner, float radius) {
    if (mCornerRadii[corner] == radius) {
      return;
    }
    mCornerRadii[corner] = radius;

    updateDrawableAttrs();
    updateBackgroundDrawableAttrs(false);
    invalidate();
  }

  /**
   * Set the corner radii of each corner individually. Currently only one unique nonzero value is
   * supported.
   *
   * @param topLeft radius of the top left corner in px.
   * @param topRight radius of the top right corner in px.
   * @param bottomRight radius of the bottom right corner in px.
   * @param bottomLeft radius of the bottom left corner in px.
   */
  public void setCornerRadius(float topLeft, float topRight, float bottomLeft, float bottomRight) {
    if (mCornerRadii[CORNER_TOP_LEFT] == topLeft
        && mCornerRadii[CORNER_TOP_RIGHT] == topRight
        && mCornerRadii[CORNER_BOTTOM_RIGHT] == bottomRight
        && mCornerRadii[CORNER_BOTTOM_LEFT] == bottomLeft) {
      return;
    }

    mCornerRadii[CORNER_TOP_LEFT] = topLeft;
    mCornerRadii[CORNER_TOP_RIGHT] = topRight;
    mCornerRadii[CORNER_BOTTOM_LEFT] = bottomLeft;
    mCornerRadii[CORNER_BOTTOM_RIGHT] = bottomRight;

    updateDrawableAttrs();
    updateBackgroundDrawableAttrs(false);
    invalidate();
  }

  public float getBorderWidth() {
    return mBorderWidth;
  }

  public void setBorderWidth(@DimenRes int resId) {
    setBorderWidth(getResources().getDimension(resId));
  }

  public void setBorderWidth(float width) {
    if (mBorderWidth == width) { return; }

    mBorderWidth = width;
    updateDrawableAttrs();
    updateBackgroundDrawableAttrs(false);
    invalidate();
  }

  public int getBorderColor() {
    return mBorderColor.getDefaultColor();
  }

  public void setBorderColor(@ColorRes int color) {
    setBorderColor(ColorStateList.valueOf(color));
  }

  public ColorStateList getBorderColors() {
    return mBorderColor;
  }

  public void setBorderColor(ColorStateList colors) {
    if (mBorderColor.equals(colors)) { return; }

    mBorderColor =
        (colors != null) ? colors : ColorStateList.valueOf(RoundedDrawable.DEFAULT_BORDER_COLOR);
    updateDrawableAttrs();
    updateBackgroundDrawableAttrs(false);
    if (mBorderWidth > 0) {
      invalidate();
    }
  }

  public boolean isOval() {
    return mIsOval;
  }

  public void setOval(boolean oval) {
    mIsOval = oval;
    updateDrawableAttrs();
    updateBackgroundDrawableAttrs(false);
    invalidate();
  }

  public Shader.TileMode getTileModeX() {
    return mTileModeX;
  }

  public void setTileModeX(Shader.TileMode tileModeX) {
    if (this.mTileModeX == tileModeX) { return; }

    this.mTileModeX = tileModeX;
    updateDrawableAttrs();
    updateBackgroundDrawableAttrs(false);
    invalidate();
  }

  public Shader.TileMode getTileModeY() {
    return mTileModeY;
  }

  public void setTileModeY(Shader.TileMode tileModeY) {
    if (this.mTileModeY == tileModeY) { return; }

    this.mTileModeY = tileModeY;
    updateDrawableAttrs();
    updateBackgroundDrawableAttrs(false);
    invalidate();
  }

  public boolean mutatesBackground() {
    return mMutateBackground;
  }

  public void mutateBackground(boolean mutate) {
    if (mMutateBackground == mutate) { return; }

    mMutateBackground = mutate;
    updateBackgroundDrawableAttrs(true);
    invalidate();
  }
}
