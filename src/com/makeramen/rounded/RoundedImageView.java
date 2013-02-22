package com.makeramen.rounded;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RoundedImageView extends ImageView {
	
	public static final int DEFAULT_RADIUS = 0;
	public static final int DEFAULT_BORDER = 0;
	public static final int DEFAULT_BORDER_COLOR = Color.BLACK;
	
	private int mRadius;
	private int mBorder;
	private int mBorderColor;
	
	public RoundedImageView(Context context) {
		super(context);
		mRadius = DEFAULT_RADIUS;
		mBorder = DEFAULT_BORDER;
		mBorderColor = DEFAULT_BORDER_COLOR;
	}

	public RoundedImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RoundedImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundedImageView, defStyle, 0);
		
		mRadius = a.getDimensionPixelSize(R.styleable.RoundedImageView_corner_radius, -1);
		mBorder = a.getDimensionPixelSize(R.styleable.RoundedImageView_border, -1);
		
		// don't allow negative values for radius and border
		if (mRadius < 0) { mRadius = DEFAULT_RADIUS; }
		if (mBorder < 0) { mBorder = DEFAULT_BORDER; }
		
		mBorderColor = a.getColor(R.styleable.RoundedImageView_border_color, DEFAULT_BORDER_COLOR);
		
		a.recycle();
	}
	
	@Override
	public void setImageDrawable(Drawable drawable) {
		super.setImageDrawable(RoundedDrawable.fromDrawable(drawable, mRadius, mBorder, mBorderColor));
	}
	
	public void setImageBitmap(Bitmap bm) {
		super.setImageDrawable(new RoundedDrawable(bm, mRadius, mBorder, mBorderColor));
    }

	@SuppressWarnings("deprecation")
	@Override
	public void setBackground(Drawable background) {
		super.setBackgroundDrawable(background);
	}

	@Override
	@Deprecated
	public void setBackgroundDrawable(Drawable background) {
		super.setBackgroundDrawable(RoundedDrawable.fromDrawable(background, mRadius, mBorder, mBorderColor));
	}

	public int getRadius() {
		return mRadius;
	}

	public int getBorder() {
		return mBorder;
	}

	public int getBorderColor() {
		return mBorderColor;
	}

	public void setRadius(int mRadius) {
		this.mRadius = mRadius;
	}

	public void setBorder(int mBorder) {
		this.mBorder = mBorder;
	}

	public void setBorderColor(int mBorderColor) {
		this.mBorderColor = mBorderColor;
	}
}
