/*
	Copyright (C) 2013 Make Ramen, LLC
*/

package com.makeramen.rounded;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.Log;
import android.widget.ImageView.ScaleType;

public class RoundedDrawable extends Drawable {
	public static final String TAG = "RoundedDrawable";

	private final RectF mBounds = new RectF();
	
	private final RectF mDrawableRect = new RectF();
	private float mCornerRadius;
	
	private final RectF mBitmapRect = new RectF();
	private final BitmapShader mBitmapShader;
	private final Paint mBitmapPaint;
	private final int mBitmapWidth;
	private final int mBitmapHeight;
	
	private final RectF mBorderRect = new RectF();
	private final Paint mBorderPaint;
	private int mBorderWidth;
	private int mBorderColor;
	
	private ScaleType mScaleType = ScaleType.FIT_XY;
	
	private final Matrix mShaderMatrix = new Matrix();
	
	RoundedDrawable(Bitmap bitmap, float cornerRadius, int border, int borderColor) {
		
		mBorderWidth = border;
		mBorderColor = borderColor; 
		
		mBitmapWidth = bitmap.getWidth();
		mBitmapHeight = bitmap.getHeight();
		mBitmapRect.set(0, 0, mBitmapWidth, mBitmapHeight);
		
		mCornerRadius = cornerRadius;
		mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
		mBitmapShader.setLocalMatrix(mShaderMatrix);

		mBitmapPaint = new Paint();
		mBitmapPaint.setAntiAlias(true);
		mBitmapPaint.setShader(mBitmapShader);
		
		mBorderPaint = new Paint();
		mBorderPaint.setAntiAlias(true);
		mBorderPaint.setColor(mBorderColor);
		mBorderPaint.setStrokeWidth(border);
	}
	
	protected void setScaleType(ScaleType scaleType) {
		if (scaleType == null) { scaleType = ScaleType.FIT_XY; }
		if (mScaleType != scaleType) {
			mScaleType = scaleType;
			setMatrix();
		}
	}
	
	protected ScaleType getScaleType() {
		return mScaleType;
	}
	
	private void setMatrix() {
		mBorderRect.set(mBounds);
		mDrawableRect.set(0 + mBorderWidth, 0 + mBorderWidth, mBorderRect.width() - mBorderWidth, mBorderRect.height() - mBorderWidth);
		
		float scale;
        float dx;
        float dy;
        
		switch(mScaleType) {
		case CENTER:
//			Log.d(TAG, "CENTER");
			mBorderRect.set(mBounds);
			mDrawableRect.set(0 + mBorderWidth, 0 + mBorderWidth, mBorderRect.width() - mBorderWidth, mBorderRect.height() - mBorderWidth);
			
			mShaderMatrix.set(null);
			mShaderMatrix.setTranslate((int) ((mDrawableRect.width() - mBitmapWidth) * 0.5f + 0.5f), (int) ((mDrawableRect.height() - mBitmapHeight) * 0.5f + 0.5f));
			break;
		case CENTER_CROP:
//			Log.d(TAG, "CENTER_CROP");
			mBorderRect.set(mBounds);
			mDrawableRect.set(0 + mBorderWidth, 0 + mBorderWidth, mBorderRect.width() - mBorderWidth, mBorderRect.height() - mBorderWidth);
			
			mShaderMatrix.set(null);

            dx = 0;
            dy = 0;

            if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
                scale = (float) mDrawableRect.height() / (float) mBitmapHeight; 
                dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
            } else {
                scale = (float) mDrawableRect.width() / (float) mBitmapWidth;
                dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
            }

            mShaderMatrix.setScale(scale, scale);
            mShaderMatrix.postTranslate((int) (dx + 0.5f) + mBorderWidth, (int) (dy + 0.5f) + mBorderWidth);
			break;
		case CENTER_INSIDE:
//			Log.d(TAG, "CENTER_INSIDE");
			mShaderMatrix.set(null);
            
            if (mBitmapWidth <= mBounds.width() && mBitmapHeight <= mBounds.height()) {
                scale = 1.0f;
            } else {
                scale = Math.min((float) mBounds.width() / (float) mBitmapWidth,
                        (float) mBounds.height() / (float) mBitmapHeight);
            }
            
            dx = (int) ((mBounds.width() - mBitmapWidth * scale) * 0.5f + 0.5f);
            dy = (int) ((mBounds.height() - mBitmapHeight * scale) * 0.5f + 0.5f);

            mShaderMatrix.setScale(scale, scale);
            mShaderMatrix.postTranslate(dx, dy);
            
    		mBorderRect.set(mBitmapRect);
            mShaderMatrix.mapRect(mBorderRect);
            mDrawableRect.set(mBorderRect.left + mBorderWidth, mBorderRect.top + mBorderWidth, mBorderRect.right - mBorderWidth, mBorderRect.bottom - mBorderWidth);
            mShaderMatrix.setRectToRect(mBitmapRect, mDrawableRect, Matrix.ScaleToFit.FILL);
            break;
		case FIT_CENTER:
			mBorderRect.set(mBitmapRect);
			mShaderMatrix.setRectToRect(mBitmapRect, mBounds, Matrix.ScaleToFit.CENTER);
			mShaderMatrix.mapRect(mBorderRect);
			mDrawableRect.set(mBorderRect.left + mBorderWidth, mBorderRect.top + mBorderWidth, mBorderRect.right - mBorderWidth, mBorderRect.bottom - mBorderWidth);
			mShaderMatrix.setRectToRect(mBitmapRect, mDrawableRect, Matrix.ScaleToFit.FILL);
			break;
		case FIT_END:
			mBorderRect.set(mBitmapRect);
			mShaderMatrix.setRectToRect(mBitmapRect, mBounds, Matrix.ScaleToFit.END);
			mShaderMatrix.mapRect(mBorderRect);
			mDrawableRect.set(mBorderRect.left + mBorderWidth, mBorderRect.top + mBorderWidth, mBorderRect.right - mBorderWidth, mBorderRect.bottom - mBorderWidth);
			mShaderMatrix.setRectToRect(mBitmapRect, mDrawableRect, Matrix.ScaleToFit.FILL);
			break;
		case FIT_START:
			mBorderRect.set(mBitmapRect);
			mShaderMatrix.setRectToRect(mBitmapRect, mBounds, Matrix.ScaleToFit.START);
			mShaderMatrix.mapRect(mBorderRect);
			mDrawableRect.set(mBorderRect.left + mBorderWidth, mBorderRect.top + mBorderWidth, mBorderRect.right - mBorderWidth, mBorderRect.bottom - mBorderWidth);
			mShaderMatrix.setRectToRect(mBitmapRect, mDrawableRect, Matrix.ScaleToFit.FILL);
			break;
		case FIT_XY:
		default:
//			Log.d(TAG, "DEFAULT TO FILL");
			mBorderRect.set(mBounds);
			mDrawableRect.set(0 + mBorderWidth, 0 + mBorderWidth, mBorderRect.width() - mBorderWidth, mBorderRect.height() - mBorderWidth);
			mShaderMatrix.set(null);
			mShaderMatrix.setRectToRect(mBitmapRect, mDrawableRect, Matrix.ScaleToFit.FILL);
			break;
		}
		mBitmapShader.setLocalMatrix(mShaderMatrix);
	}

	@Override
	protected void onBoundsChange(Rect bounds) {
//		Log.i(TAG, "onboundschange: w: " + bounds.width() + "h:" + bounds.height());
		super.onBoundsChange(bounds);
		
		mBounds.set(bounds);

//		if (USE_VIGNETTE) {
//			RadialGradient vignette = new RadialGradient(
//					mDrawableRect.centerX(), mDrawableRect.centerY() * 1.0f / 0.7f, mDrawableRect.centerX() * 1.3f,
//					new int[] { 0, 0, 0x7f000000 }, new float[] { 0.0f, 0.7f, 1.0f },
//					Shader.TileMode.CLAMP);
//
//			Matrix oval = new Matrix();
//			oval.setScale(1.0f, 0.7f);
//			vignette.setLocalMatrix(oval);
//
//			mBitmapPaint.setShader(
//					new ComposeShader(mBitmapShader, vignette, PorterDuff.Mode.SRC_OVER));
//		}
		
		setMatrix();
	}

	@Override
	public void draw(Canvas canvas) {
//		Log.w(TAG, "Draw: " + mScaleType.toString());
		if (mBorderWidth > 0) {
			canvas.drawRoundRect(mBorderRect, mCornerRadius, mCornerRadius, mBorderPaint);
			canvas.drawRoundRect(mDrawableRect, Math.max(mCornerRadius - mBorderWidth, 0), Math.max(mCornerRadius - mBorderWidth, 0), mBitmapPaint);
		} else {
			canvas.drawRoundRect(mDrawableRect, mCornerRadius, mCornerRadius, mBitmapPaint);
		}
	}

	@Override
	public int getOpacity() {
		return PixelFormat.TRANSLUCENT;
	}

	@Override
	public void setAlpha(int alpha) {
		mBitmapPaint.setAlpha(alpha);
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		mBitmapPaint.setColorFilter(cf);
	}

	@Override
	public int getIntrinsicWidth() {
		return mBitmapWidth;
	}
	
	@Override
	public int getIntrinsicHeight() {
		return mBitmapHeight;
	}
	
	public static Bitmap drawableToBitmap(Drawable drawable) {
	    if (drawable instanceof BitmapDrawable) {
	        return ((BitmapDrawable)drawable).getBitmap();
	    }
	    
	    Bitmap bitmap;
	    int width = drawable.getIntrinsicWidth();
	    int height = drawable.getIntrinsicHeight();
	    if (width > 0 && height > 0) {
		    bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		    Canvas canvas = new Canvas(bitmap); 
		    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		    drawable.draw(canvas);
	    } else {
	    	bitmap = null;
	    }

	    return bitmap;
	}
	
	public static Drawable fromDrawable(Drawable drawable, float radius) {
		return fromDrawable(drawable, radius, 0, 0);
	}
	
	public static Drawable fromDrawable(Drawable drawable, float radius, int border, int borderColor) {
		if (drawable != null) {
			if (drawable instanceof TransitionDrawable) {
		    	TransitionDrawable td = (TransitionDrawable) drawable;
		    	int num = td.getNumberOfLayers();
		    	
		    	Drawable[] drawableList = new Drawable[num];
		    	for (int i = 0; i < num; i++) {
		    		Drawable d = td.getDrawable(i);
		    		if (d instanceof ColorDrawable) {
		    			// TODO skip colordrawables for now
		    			drawableList[i] = d;
		    		} else {
			    		drawableList[i] = new RoundedDrawable(drawableToBitmap(td.getDrawable(i)), radius, border, borderColor);
		    		}
		    	}
		    	return new TransitionDrawable(drawableList);
		    } 
			
			Bitmap bm = drawableToBitmap(drawable);
			if (bm != null) {
				return new RoundedDrawable(bm, radius, border, borderColor);
			} else {
				Log.w(TAG, "Failed to create bitmap from drawable!");
			}
		}
		return drawable;
	}

	public float getCornerRadius() {
		return mCornerRadius;
	}

	public int getBorderWidth() {
		return mBorderWidth;
	}

	public int getBorderColor() {
		return mBorderColor;
	}

	public void setCornerRadius(float radius) {
		this.mCornerRadius = radius;
	}

	public void setBorderWidth(int width) {
		this.mBorderWidth = width;
		mBorderPaint.setStrokeWidth(mBorderWidth);
	}

	public void setBorderColor(int color) {
		this.mBorderColor = color;
		mBorderPaint.setColor(color);
	}
}