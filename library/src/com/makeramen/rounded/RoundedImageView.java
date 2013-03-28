/*
	Copyright (C) 2013 Make Ramen, LLC
*/

package com.makeramen.rounded;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RoundedImageView extends ImageView {
	
	public static final String TAG = "RoundedImageView";
	
	public static final int DEFAULT_RADIUS = 0;
	public static final int DEFAULT_BORDER = 0;
	public static final int DEFAULT_BORDER_COLOR = Color.BLACK;
	
	private int mCornerRadius;
	private int mBorderWidth;
	private int mBorderColor;
	
	private boolean roundBackground;
	
	private Drawable mDrawable;
	private Drawable mBackgroundDrawable;
	
	private ScaleType mScaleType;

    private static final ScaleType[] sScaleTypeArray = {
            ScaleType.MATRIX,
            ScaleType.FIT_XY,
            ScaleType.FIT_START,
            ScaleType.FIT_CENTER,
            ScaleType.FIT_END,
            ScaleType.CENTER,
            ScaleType.CENTER_CROP,
            ScaleType.CENTER_INSIDE
    };
	
	public RoundedImageView(Context context) {
		super(context);
		mCornerRadius = DEFAULT_RADIUS;
		mBorderWidth = DEFAULT_BORDER;
		mBorderColor = DEFAULT_BORDER_COLOR;
	}

	public RoundedImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RoundedImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundedImageView, defStyle, 0);

        int index = a.getInt(R.styleable.RoundedImageView_android_scaleType, -1);
        if (index >= 0) {
            setScaleType(sScaleTypeArray[index]);
        }
		
		mCornerRadius = a.getDimensionPixelSize(R.styleable.RoundedImageView_corner_radius, -1);
		mBorderWidth = a.getDimensionPixelSize(R.styleable.RoundedImageView_border_width, -1);
		
		// don't allow negative values for radius and border
		if (mCornerRadius < 0) { mCornerRadius = DEFAULT_RADIUS; }
		if (mBorderWidth < 0) { mBorderWidth = DEFAULT_BORDER; }
		
		mBorderColor = a.getColor(R.styleable.RoundedImageView_border_color, DEFAULT_BORDER_COLOR);
		
		roundBackground = a.getBoolean(R.styleable.RoundedImageView_round_background, false);
		
		a.recycle();
	}
	
	/**
     * Controls how the image should be resized or moved to match the size
     * of this ImageView.
     * 
     * @param scaleType The desired scaling mode.
     * 
     * @attr ref android.R.styleable#ImageView_scaleType
     */
	@Override
    public void setScaleType(ScaleType scaleType) {
        if (scaleType == null) {
            throw new NullPointerException();
        }
        
        if (mScaleType != scaleType) {
	        mScaleType = scaleType;
        
	        switch(scaleType) {
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
	        
			if (mDrawable instanceof RoundedDrawable
					&& ((RoundedDrawable) mDrawable).getScaleType() != scaleType) {
				((RoundedDrawable) mDrawable).setScaleType(scaleType);
			}
	        
			if (mBackgroundDrawable instanceof RoundedDrawable
					&& ((RoundedDrawable) mBackgroundDrawable).getScaleType() != scaleType) {
				((RoundedDrawable) mBackgroundDrawable).setScaleType(scaleType);
			}
			setWillNotCacheDrawing(true);
			requestLayout();
			invalidate();
        }
    }
	
	/**
     * Return the current scale type in use by this ImageView.
     *
     * @see ImageView.ScaleType
     *
     * @attr ref android.R.styleable#ImageView_scaleType
     */
	@Override
    public ScaleType getScaleType() {
        return mScaleType;
    }
	
	
	@Override
	public void setImageDrawable(Drawable drawable) {
		if (drawable != null) {
			mDrawable = RoundedDrawable.fromDrawable(drawable, mCornerRadius, mBorderWidth, mBorderColor);
			((RoundedDrawable) mDrawable).setScaleType(mScaleType);
			((RoundedDrawable) mDrawable).setCornerRadius(mCornerRadius);
			((RoundedDrawable) mDrawable).setBorderWidth(mBorderWidth);
			((RoundedDrawable) mDrawable).setBorderColor(mBorderColor);
		} else {
			 mDrawable = null;
		}
		super.setImageDrawable(mDrawable);
	}
	
	public void setImageBitmap(Bitmap bm) {
		if (bm != null) {
			mDrawable = new RoundedDrawable(bm, mCornerRadius, mBorderWidth, mBorderColor);
			((RoundedDrawable) mDrawable).setScaleType(mScaleType);
			((RoundedDrawable) mDrawable).setCornerRadius(mCornerRadius);
			((RoundedDrawable) mDrawable).setBorderWidth(mBorderWidth);
			((RoundedDrawable) mDrawable).setBorderColor(mBorderColor);
		} else {
			mDrawable = null;
		}
		super.setImageDrawable(mDrawable);
    }

	@Override
	public void setBackground(Drawable background) {
		setBackgroundDrawable(background);
	}

	@Override
	@Deprecated
	public void setBackgroundDrawable(Drawable background) {
		if (roundBackground && background != null) {
			mBackgroundDrawable = RoundedDrawable.fromDrawable(background, mCornerRadius, mBorderWidth, mBorderColor);
			((RoundedDrawable) mBackgroundDrawable).setScaleType(mScaleType);
			((RoundedDrawable) mBackgroundDrawable).setCornerRadius(mCornerRadius);
			((RoundedDrawable) mBackgroundDrawable).setBorderWidth(mBorderWidth);
			((RoundedDrawable) mBackgroundDrawable).setBorderColor(mBorderColor);
		} else {
			mBackgroundDrawable = background;
		}
		super.setBackgroundDrawable(mBackgroundDrawable);
	}

	public int getCornerRadius() {
		return mCornerRadius;
	}

	public int getBorder() {
		return mBorderWidth;
	}

	public int getBorderColor() {
		return mBorderColor;
	}

	public void setCornerRadius(int radius) {
		if (mCornerRadius == radius) { return; }
		
		this.mCornerRadius = radius;
		if (mDrawable instanceof RoundedDrawable) {
			((RoundedDrawable) mDrawable).setCornerRadius(radius);
		}
		if (roundBackground && mBackgroundDrawable instanceof RoundedDrawable) {
			((RoundedDrawable) mBackgroundDrawable).setCornerRadius(radius);
		}
	}

	public void setBorderWidth(int width) {
		if (mBorderWidth == width) { return; }
		
		this.mBorderWidth = width;
		if (mDrawable instanceof RoundedDrawable) {
			((RoundedDrawable) mDrawable).setBorderWidth(width);
		}
		if (roundBackground && mBackgroundDrawable instanceof RoundedDrawable) {
			((RoundedDrawable) mBackgroundDrawable).setBorderWidth(width);
		}
		invalidate();
	}

	public void setBorderColor(int color) {
		if (mBorderColor == color) { return; }
		
		this.mBorderColor = color;
		if (mDrawable instanceof RoundedDrawable) {
			((RoundedDrawable) mDrawable).setBorderColor(color);
		}
		if (roundBackground && mBackgroundDrawable instanceof RoundedDrawable) {
			((RoundedDrawable) mBackgroundDrawable).setBorderColor(color);
		}
		if (mBorderWidth > 0) { invalidate(); }
	}

	public boolean isRoundBackground() {
		return roundBackground;
	}

	public void setRoundBackground(boolean roundBackground) {
		if (this.roundBackground == roundBackground) { return; }
		
		this.roundBackground = roundBackground;
		if (roundBackground) {
			if (mBackgroundDrawable instanceof RoundedDrawable) {
				((RoundedDrawable) mBackgroundDrawable).setScaleType(mScaleType);
				((RoundedDrawable) mBackgroundDrawable).setCornerRadius(mCornerRadius);
				((RoundedDrawable) mBackgroundDrawable).setBorderWidth(mBorderWidth);
				((RoundedDrawable) mBackgroundDrawable).setBorderColor(mBorderColor);
			} else {
				setBackgroundDrawable(mBackgroundDrawable);
			}
		} else if (mBackgroundDrawable instanceof RoundedDrawable) {
			((RoundedDrawable) mBackgroundDrawable).setBorderWidth(0);
			((RoundedDrawable) mBackgroundDrawable).setCornerRadius(0);
		}
		
		invalidate();
	}
}
