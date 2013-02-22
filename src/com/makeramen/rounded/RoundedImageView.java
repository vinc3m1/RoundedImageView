package com.makeramen.rounded;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
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
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class RoundedImageView extends ImageView {
	
	public static final int DEFAULT_RADIUS = 0;
	public static final int DEFAULT_BORDER = 0;
	public static final int DEFAULT_BORDER_COLOR = Color.BLACK;
	
	private final int mRadius;
	private final int mBorder;
	private final int mBorderColor;
	
	public RoundedImageView(Context context) {
		super(context);
		mRadius = DEFAULT_RADIUS;
		mBorder = DEFAULT_BORDER;
		mBorderColor = DEFAULT_BORDER_COLOR;
	}

	public RoundedImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	@SuppressWarnings("deprecation")
	public RoundedImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundedImageView, defStyle, 0);
		
		int radius = a.getDimensionPixelSize(R.styleable.RoundedImageView_corner_radius, -1);
		int border = a.getDimensionPixelSize(R.styleable.RoundedImageView_border, -1);
		
		mRadius = radius >= 0 ? radius : DEFAULT_RADIUS; // ensure non-negative;
		mBorder = border >= 0 ? border : DEFAULT_BORDER; // ensure non-negative;
		
		mBorderColor = a.getColor(R.styleable.RoundedImageView_border_color, DEFAULT_BORDER_COLOR);
		
		if (a.getBoolean(R.styleable.RoundedImageView_round_background, false)) {
			Drawable bg = getBackground();
			if (bg != null){
				setBackgroundDrawable(new RoundedDrawable(drawableToBitmap(bg), mRadius, mBorder, mBorderColor));
			}
		}
		
		a.recycle();
	}
	
	@Override
	public void setImageDrawable(Drawable drawable) {
		if (drawable != null) {
			if (drawable instanceof TransitionDrawable) {
		    	TransitionDrawable td = (TransitionDrawable) drawable;
		    	int num = td.getNumberOfLayers();
		    	
		    	Drawable[] drawableList = new Drawable[num];
		    	for (int i = 0; i < num; i++) {
		    		Drawable d = td.getDrawable(i);
		    		if (d instanceof ColorDrawable) {
		    			drawableList[i] = d;
		    		} else {
			    		drawableList[i] = new RoundedDrawable(drawableToBitmap(td.getDrawable(i)), mRadius, mBorder, mBorderColor);
		    		}
		    	}
		    	super.setImageDrawable(new TransitionDrawable(drawableList));
				return;
		    } 
			
			Bitmap bm = drawableToBitmap(drawable);
			if (bm != null) {
				super.setImageDrawable(new RoundedDrawable(bm, mRadius, mBorder, mBorderColor));
				return;
			}
		}
		super.setImageDrawable(null);
	}
	
	public static Bitmap drawableToBitmap (Drawable drawable) {
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
	    	Log.i("vmi", "wtf intrinsics are invalid");
	    	bitmap = null;
	    }

	    return bitmap;
	}
	
	public static class RoundedDrawable extends Drawable {
		private static final boolean USE_VIGNETTE = false;

		private final float mCornerRadius;
		private final RectF mDrawableRect = new RectF();
		private final RectF mBorderRect = new RectF();
		private final RectF mBitmapRect = new RectF();
		private final BitmapShader mBitmapShader;
		private final Paint mBorderPaint;
		private final Paint mBitmapPaint;
		private final int mBorder;
		private final int mBorderColor;
		
		private final Matrix mShaderMatrix = new Matrix();
		
		RoundedDrawable(Bitmap bitmap, float cornerRadius, int border, int borderColor) {
			mBorder = border;
			mBorderColor = borderColor; 
			
			mBitmapRect.set(0, 0, bitmap.getWidth(), bitmap.getHeight());
			
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
	}

}
