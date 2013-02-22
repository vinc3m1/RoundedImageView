package com.makeramen.rounded;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class RoundedActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_stream);

		StreamAdapter adapter = new StreamAdapter(this);
		((ListView) findViewById(R.id.main_list)).setAdapter(adapter);

		adapter.add(new StreamItem(this, R.drawable.photo1, "Tufa at night", "Mono Lake, CA"));
		adapter.add(new StreamItem(this, R.drawable.photo2, "Starry night", "Lake Powell, AZ"));
		adapter.add(new StreamItem(this, R.drawable.photo3, "Racetrack playa", "Death Valley, CA"));
		adapter.add(new StreamItem(this, R.drawable.photo4, "Napali coast", "Kauai, HI"));
		adapter.add(new StreamItem(this, R.drawable.photo5, "Delicate Arch", "Arches, UT"));
		adapter.add(new StreamItem(this, R.drawable.photo6, "Sierra sunset", "Lone Pine, CA"));
		adapter.add(new StreamItem(this, R.drawable.photo7, "Majestic", "Grand Teton, WY"));
	}

	class StreamItem {
		final Bitmap mBitmap;
		final String mLine1;
		final String mLine2;

		StreamItem(Context c, int resid, String line1, String line2) {
			mBitmap = BitmapFactory.decodeResource(c.getResources(), resid);
			mLine1 = line1;
			mLine2 = line2;
		}
	}

	class StreamDrawable extends Drawable {
		private static final boolean USE_VIGNETTE = true;

		private final float mCornerRadius;
		private final RectF mRect = new RectF();
		private final BitmapShader mBitmapShader;
		private final Paint mPaint;
		private final int mMargin;

		StreamDrawable(Bitmap bitmap, float cornerRadius, int margin) {
			mCornerRadius = cornerRadius;

			mBitmapShader = new BitmapShader(bitmap,
					Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

			mPaint = new Paint();
			mPaint.setAntiAlias(true);
			mPaint.setShader(mBitmapShader);

			mMargin = margin;
		}

		@Override
		protected void onBoundsChange(Rect bounds) {
			super.onBoundsChange(bounds);
			mRect.set(mMargin, mMargin, bounds.width() - mMargin, bounds.height() - mMargin);

			if (USE_VIGNETTE) {
				RadialGradient vignette = new RadialGradient(
						mRect.centerX(), mRect.centerY() * 1.0f / 0.7f, mRect.centerX() * 1.3f,
						new int[] { 0, 0, 0x7f000000 }, new float[] { 0.0f, 0.7f, 1.0f },
						Shader.TileMode.CLAMP);
	
				Matrix oval = new Matrix();
				oval.setScale(1.0f, 0.7f);
				vignette.setLocalMatrix(oval);
	
				mPaint.setShader(
						new ComposeShader(mBitmapShader, vignette, PorterDuff.Mode.SRC_OVER));
			}
		}

		@Override
		public void draw(Canvas canvas) {
			canvas.drawRoundRect(mRect, mCornerRadius, mCornerRadius, mPaint);
		}

		@Override
		public int getOpacity() {
			return PixelFormat.TRANSLUCENT;
		}

		@Override
		public void setAlpha(int alpha) {
			mPaint.setAlpha(alpha);
		}

		@Override
		public void setColorFilter(ColorFilter cf) {
			mPaint.setColorFilter(cf);
		}		
	}


	class StreamAdapter extends ArrayAdapter<StreamItem> {
		private static final int CORNER_RADIUS = 24; // dips
		private static final int MARGIN = 12; // dips

		private final int mCornerRadius;
		private final int mMargin;
		private final LayoutInflater mInflater;

		public StreamAdapter(Context context) {
			super(context, 0);
			
			final float density = context.getResources().getDisplayMetrics().density;
			mCornerRadius = (int) (CORNER_RADIUS * density + 0.5f);
			mMargin = (int) (MARGIN * density + 0.5f);

			mInflater = LayoutInflater.from(getContext());
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewGroup view = null;
			
			if (convertView == null) {
				view = (ViewGroup) mInflater.inflate(R.layout.stream_item, parent, false);
			} else {
				view = (ViewGroup) convertView;
			}

			StreamItem item = getItem(position);

			StreamDrawable d = new StreamDrawable(item.mBitmap, mCornerRadius, mMargin);
			view.setBackground(d);

			((TextView) view.findViewById(R.id.textView1)).setText(item.mLine1);
			((TextView) view.findViewById(R.id.textView2)).setText(item.mLine2);

			int w = item.mBitmap.getWidth();
			int h = item.mBitmap.getHeight();

			float ratio = w / (float) h;

			LayoutParams lp = view.getLayoutParams();
			lp.width = getContext().getResources().getDisplayMetrics().widthPixels;
			lp.height = (int) (lp.width / ratio);

			return view;
		}
	}
}
