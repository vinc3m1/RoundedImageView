package com.makeramen;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.widget.ImageView.ScaleType;

@SuppressWarnings("UnusedDeclaration")
public class RoundedDrawable extends Drawable {

  public static final String TAG = "RoundedDrawable";
  public static final int DEFAULT_BORDER_COLOR = Color.BLACK;

  private final RectF mBounds = new RectF();
  private final RectF mDrawableRect = new RectF();
  private final RectF mBitmapRect = new RectF();
  private final BitmapShader mBitmapShader;
  private final Paint mBitmapPaint;
  private final int mBitmapWidth;
  private final int mBitmapHeight;
  private final RectF mBorderRect = new RectF();
  private final Paint mBorderPaint;
  private final Matrix mShaderMatrix = new Matrix();

  private float mCornerRadius = 0;
  private boolean mOval = false;
  private float mBorderWidth = 0;
  private ColorStateList mBorderColor = ColorStateList.valueOf(DEFAULT_BORDER_COLOR);
  private ScaleType mScaleType = ScaleType.FIT_CENTER;

  public RoundedDrawable(Bitmap bitmap) {

    mBitmapWidth = bitmap.getWidth();
    mBitmapHeight = bitmap.getHeight();
    mBitmapRect.set(0, 0, mBitmapWidth, mBitmapHeight);

    mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
    mBitmapShader.setLocalMatrix(mShaderMatrix);

    mBitmapPaint = new Paint();
    mBitmapPaint.setStyle(Paint.Style.FILL);
    mBitmapPaint.setAntiAlias(true);
    mBitmapPaint.setShader(mBitmapShader);

    mBorderPaint = new Paint();
    mBorderPaint.setStyle(Paint.Style.STROKE);
    mBorderPaint.setAntiAlias(true);
    mBorderPaint.setColor(mBorderColor.getColorForState(getState(), DEFAULT_BORDER_COLOR));
    mBorderPaint.setStrokeWidth(mBorderWidth);
  }

  public static RoundedDrawable fromBitmap(Bitmap bitmap) {
    if (bitmap != null) {
      return new RoundedDrawable(bitmap);
    } else {
      return null;
    }
  }

  public static Drawable fromDrawable(Drawable drawable) {
    if (drawable != null) {
      if (drawable instanceof RoundedDrawable) {
        // just return if it's already a RoundedDrawable
        return drawable;
      } else if (drawable instanceof LayerDrawable) {
        LayerDrawable ld = (LayerDrawable) drawable;
        int num = ld.getNumberOfLayers();

        // loop through layers to and change to RoundedDrawables if possible
        for (int i = 0; i < num; i++) {
          Drawable d = ld.getDrawable(i);
          ld.setDrawableByLayerId(ld.getId(i), fromDrawable(d));
        }
        return ld;
      }

      // try to get a bitmap from the drawable and
      Bitmap bm = drawableToBitmap(drawable);
      if (bm != null) {
        return new RoundedDrawable(bm);
      } else {
        Log.w(TAG, "Failed to create bitmap from drawable!");
      }
    }
    return drawable;
  }

  public static Bitmap drawableToBitmap(Drawable drawable) {
    if (drawable instanceof BitmapDrawable) {
      return ((BitmapDrawable) drawable).getBitmap();
    }

    Bitmap bitmap;
    int width = Math.max(drawable.getIntrinsicWidth(), 1);
    int height = Math.max(drawable.getIntrinsicHeight(), 1);
    try {
      bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
      Canvas canvas = new Canvas(bitmap);
      drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
      drawable.draw(canvas);
    } catch (Exception e) {
      e.printStackTrace();
      bitmap = null;
    }

    return bitmap;
  }

  @Override
  public boolean isStateful() {
    return mBorderColor.isStateful();
  }

  @Override
  protected boolean onStateChange(int[] state) {
    int newColor = mBorderColor.getColorForState(state, 0);
    if (mBorderPaint.getColor() != newColor) {
      mBorderPaint.setColor(newColor);
      return true;
    } else {
      return super.onStateChange(state);
    }
  }

  private void updateShaderMatrix() {
    float scale;
    float dx;
    float dy;

    switch (mScaleType) {
      case CENTER:
        mBorderRect.set(mBounds);
        mBorderRect.inset((mBorderWidth) / 2, (mBorderWidth) / 2);

        mShaderMatrix.set(null);
        mShaderMatrix.setTranslate((int) ((mBorderRect.width() - mBitmapWidth) * 0.5f + 0.5f),
            (int) ((mBorderRect.height() - mBitmapHeight) * 0.5f + 0.5f));
        break;

      case CENTER_CROP:
        mBorderRect.set(mBounds);
        mBorderRect.inset((mBorderWidth) / 2, (mBorderWidth) / 2);

        mShaderMatrix.set(null);

        dx = 0;
        dy = 0;

        if (mBitmapWidth * mBorderRect.height() > mBorderRect.width() * mBitmapHeight) {
          scale = mBorderRect.height() / (float) mBitmapHeight;
          dx = (mBorderRect.width() - mBitmapWidth * scale) * 0.5f;
        } else {
          scale = mBorderRect.width() / (float) mBitmapWidth;
          dy = (mBorderRect.height() - mBitmapHeight * scale) * 0.5f;
        }

        mShaderMatrix.setScale(scale, scale);
        mShaderMatrix.postTranslate((int) (dx + 0.5f) + mBorderWidth,
            (int) (dy + 0.5f) + mBorderWidth);
        break;

      case CENTER_INSIDE:
        mShaderMatrix.set(null);

        if (mBitmapWidth <= mBounds.width() && mBitmapHeight <= mBounds.height()) {
          scale = 1.0f;
        } else {
          scale = Math.min(mBounds.width() / (float) mBitmapWidth,
              mBounds.height() / (float) mBitmapHeight);
        }

        dx = (int) ((mBounds.width() - mBitmapWidth * scale) * 0.5f + 0.5f);
        dy = (int) ((mBounds.height() - mBitmapHeight * scale) * 0.5f + 0.5f);

        mShaderMatrix.setScale(scale, scale);
        mShaderMatrix.postTranslate(dx, dy);

        mBorderRect.set(mBitmapRect);
        mShaderMatrix.mapRect(mBorderRect);
        mBorderRect.inset((mBorderWidth) / 2, (mBorderWidth) / 2);
        mShaderMatrix.setRectToRect(mBitmapRect, mBorderRect, Matrix.ScaleToFit.FILL);
        break;

      default:
      case FIT_CENTER:
        mBorderRect.set(mBitmapRect);
        mShaderMatrix.setRectToRect(mBitmapRect, mBounds, Matrix.ScaleToFit.CENTER);
        mShaderMatrix.mapRect(mBorderRect);
        mBorderRect.inset((mBorderWidth) / 2, (mBorderWidth) / 2);
        mShaderMatrix.setRectToRect(mBitmapRect, mBorderRect, Matrix.ScaleToFit.FILL);
        break;

      case FIT_END:
        mBorderRect.set(mBitmapRect);
        mShaderMatrix.setRectToRect(mBitmapRect, mBounds, Matrix.ScaleToFit.END);
        mShaderMatrix.mapRect(mBorderRect);
        mBorderRect.inset((mBorderWidth) / 2, (mBorderWidth) / 2);
        mShaderMatrix.setRectToRect(mBitmapRect, mBorderRect, Matrix.ScaleToFit.FILL);
        break;

      case FIT_START:
        mBorderRect.set(mBitmapRect);
        mShaderMatrix.setRectToRect(mBitmapRect, mBounds, Matrix.ScaleToFit.START);
        mShaderMatrix.mapRect(mBorderRect);
        mBorderRect.inset((mBorderWidth) / 2, (mBorderWidth) / 2);
        mShaderMatrix.setRectToRect(mBitmapRect, mBorderRect, Matrix.ScaleToFit.FILL);
        break;

      case FIT_XY:
        mBorderRect.set(mBounds);
        mBorderRect.inset((mBorderWidth) / 2, (mBorderWidth) / 2);
        mShaderMatrix.set(null);
        mShaderMatrix.setRectToRect(mBitmapRect, mBorderRect, Matrix.ScaleToFit.FILL);
        break;
    }

    mDrawableRect.set(mBorderRect);
    mBitmapShader.setLocalMatrix(mShaderMatrix);
  }

  @Override
  protected void onBoundsChange(Rect bounds) {
    super.onBoundsChange(bounds);

    mBounds.set(bounds);

    updateShaderMatrix();
  }

  @Override
  public void draw(Canvas canvas) {

    if (mOval) {
      if (mBorderWidth > 0) {
        canvas.drawOval(mDrawableRect, mBitmapPaint);
        canvas.drawOval(mBorderRect, mBorderPaint);
      } else {
        canvas.drawOval(mDrawableRect, mBitmapPaint);
      }
    } else {
      if (mBorderWidth > 0) {
        canvas.drawRoundRect(mDrawableRect, Math.max(mCornerRadius, 0),
            Math.max(mCornerRadius, 0), mBitmapPaint);
        canvas.drawRoundRect(mBorderRect, mCornerRadius, mCornerRadius, mBorderPaint);
      } else {
        canvas.drawRoundRect(mDrawableRect, mCornerRadius, mCornerRadius, mBitmapPaint);
      }
    }
  }

  @Override
  public int getOpacity() {
    return PixelFormat.TRANSLUCENT;
  }

  @Override
  public void setAlpha(int alpha) {
    mBitmapPaint.setAlpha(alpha);
    invalidateSelf();
  }

  @Override
  public void setColorFilter(ColorFilter cf) {
    mBitmapPaint.setColorFilter(cf);
    invalidateSelf();
  }

  @Override public void setDither(boolean dither) {
    mBitmapPaint.setDither(dither);
    invalidateSelf();
  }

  @Override public void setFilterBitmap(boolean filter) {
    mBitmapPaint.setFilterBitmap(filter);
    invalidateSelf();
  }

  @Override
  public int getIntrinsicWidth() {
    return mBitmapWidth;
  }

  @Override
  public int getIntrinsicHeight() {
    return mBitmapHeight;
  }

  public float getCornerRadius() {
    return mCornerRadius;
  }

  public RoundedDrawable setCornerRadius(float radius) {
    mCornerRadius = radius;
    return this;
  }

  public float getBorderWidth() {
    return mBorderWidth;
  }

  public RoundedDrawable setBorderWidth(int width) {
    mBorderWidth = width;
    mBorderPaint.setStrokeWidth(mBorderWidth);
    return this;
  }

  public int getBorderColor() {
    return mBorderColor.getDefaultColor();
  }

  public RoundedDrawable setBorderColor(int color) {
    return setBorderColors(ColorStateList.valueOf(color));
  }

  public ColorStateList getBorderColors() {
    return mBorderColor;
  }

  public RoundedDrawable setBorderColors(ColorStateList colors) {
    mBorderColor = colors != null ? colors : ColorStateList.valueOf(0);
    mBorderPaint.setColor(mBorderColor.getColorForState(getState(), DEFAULT_BORDER_COLOR));
    return this;
  }

  public boolean isOval() {
    return mOval;
  }

  public RoundedDrawable setOval(boolean oval) {
    mOval = oval;
    return this;
  }

  public ScaleType getScaleType() {
    return mScaleType;
  }

  public RoundedDrawable setScaleType(ScaleType scaleType) {
    if (scaleType == null) {
      scaleType = ScaleType.FIT_CENTER;
    }
    if (mScaleType != scaleType) {
      mScaleType = scaleType;
      updateShaderMatrix();
    }
    return this;
  }

  public Bitmap toBitmap() {
    return drawableToBitmap(this);
  }
}
