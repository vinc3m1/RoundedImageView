/*
	Copyright (C) 2013 Make Ramen, LLC
*/

package com.makeramen.rounded.example;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;

import com.makeramen.rounded.RoundedImageView;

public class RoundedActivity extends Activity {
	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_rounded);

		StreamAdapter adapter = new StreamAdapter(this);
		((ListView) findViewById(R.id.main_list)).setAdapter(adapter);

		adapter.add(new StreamItem(this, R.drawable.photo1, "Tufa at night", "Mono Lake, CA", ScaleType.CENTER));
		adapter.add(new StreamItem(this, R.drawable.photo2, "Starry night", "Lake Powell, AZ", ScaleType.CENTER_CROP));
		adapter.add(new StreamItem(this, R.drawable.photo3, "Racetrack playa", "Death Valley, CA", ScaleType.CENTER_INSIDE));
		adapter.add(new StreamItem(this, R.drawable.photo4, "Napali coast", "Kauai, HI", ScaleType.FIT_CENTER));
		adapter.add(new StreamItem(this, R.drawable.photo5, "Delicate Arch", "Arches, UT", ScaleType.FIT_END));
		adapter.add(new StreamItem(this, R.drawable.photo6, "Sierra sunset", "Lone Pine, CA", ScaleType.FIT_START));
		adapter.add(new StreamItem(this, R.drawable.photo7, "Majestic", "Grand Teton, WY", ScaleType.FIT_XY));
	}

	class StreamItem {
		final Bitmap mBitmap;
		final String mLine1;
		final String mLine2;
		final ScaleType mScaleType;

		StreamItem(Context c, int resid, String line1, String line2, ScaleType scaleType) {
			mBitmap = BitmapFactory.decodeResource(c.getResources(), resid);
			mLine1 = line1;
			mLine2 = line2;
			mScaleType = scaleType;
		}
	}

	class StreamAdapter extends ArrayAdapter<StreamItem> {
		private final LayoutInflater mInflater;

		public StreamAdapter(Context context) {
			super(context, 0);
			mInflater = LayoutInflater.from(getContext());
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewGroup view = null;
			
			if (convertView == null) {
				view = (ViewGroup) mInflater.inflate(R.layout.rounded_item, parent, false);
			} else {
				view = (ViewGroup) convertView;
			}

			StreamItem item = getItem(position);

			((RoundedImageView) view.findViewById(R.id.imageView1)).setImageBitmap(item.mBitmap);
			((RoundedImageView) view.findViewById(R.id.imageView1)).setScaleType(item.mScaleType);
			((TextView) view.findViewById(R.id.textView1)).setText(item.mLine1);
			((TextView) view.findViewById(R.id.textView2)).setText(item.mLine2);
			((TextView) view.findViewById(R.id.textView3)).setText(item.mScaleType.toString());
			return view;
		}
	}
}
