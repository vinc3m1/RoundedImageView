package com.makeramen.rounded;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ComposeShader;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.Log;

public class RoundedDrawable extends Drawable {
	public static final String TAG = "RoundedDrawable";
	private static final boolean USE_VIGNETTE = false;

	private final RectF mDrawableRect = new RectF();
	private final float mCornerRadius;
	
	private final RectF mBitmapRect = new RectF();
	private final BitmapShader mBitmapShader;
	private final Paint mBitmapPaint;
	private final int mBitmapWidth;
	private final int mBitmapHeight;
	
	private final RectF mBorderRect = new RectF();
	private final Paint mBorderPaint;
	private final int mBorder;
	private final int mBorderColor;
	
	private final Matrix mShaderMatrix = new Matrix();
	
	RoundedDrawable(Bitmap bitmap, float cornerRadius, int border, int borderColor) {
		mBorder = border;
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

	@Override
	protected void onBoundsChange(Rect bounds) {
		super.onBoundsChange(bounds);
		mDrawableRect.set(0 + mBorder, 0 + mBorder, bounds.width() - mBorder, bounds.height() - mBorder);
		mBorderRect.set(0, 0, bounds.width(), bounds.height());

		if (USE_VIGNETTE) {
			RadialGradient vignette = new RadialGradient(
					mDrawableRect.centerX(), mDrawableRect.centerY() * 1.0f / 0.7f, mDrawableRect.centerX() * 1.3f,
					new int[] { 0, 0, 0x7f000000 }, new float[] { 0.0f, 0.7f, 1.0f },
					Shader.TileMode.CLAMP);

			Matrix oval = new Matrix();
			oval.setScale(1.0f, 0.7f);
			vignette.setLocalMatrix(oval);

			mBitmapPaint.setShader(
					new ComposeShader(mBitmapShader, vignette, PorterDuff.Mode.SRC_OVER));
		}
		
		mShaderMatrix.setRectToRect(mBitmapRect, mDrawableRect, Matrix.ScaleToFit.FILL);
		mBitmapShader.setLocalMatrix(mShaderMatrix);
	}

	@Override
	public void draw(Canvas canvas) {
		if (mBorder > 0) {
			canvas.drawRoundRect(mBorderRect, mCornerRadius, mCornerRadius, mBorderPaint);
			canvas.drawRoundRect(mDrawableRect, mCornerRadius - mBorder, mCornerRadius - mBorder, mBitmapPaint);
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
}