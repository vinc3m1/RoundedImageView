package com.makeramen.example;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;
import com.makeramen.RoundedDrawable;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class PicassoActivity extends Activity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_rounded);

    PicassoAdapter adapter = new PicassoAdapter(this);
    ((ListView) findViewById(R.id.main_list)).setAdapter(adapter);

    adapter.add(new PicassoItem("http://24.media.tumblr.com/2176464a507f8a34f09d58ee7fcf105a/tumblr_mzgzd79XMY1st5lhmo1_1280.jpg", ScaleType.CENTER));
    adapter.add(new PicassoItem("http://25.media.tumblr.com/af50758346e388e6e69f4c378c4f264f/tumblr_mzgzcdEDTL1st5lhmo1_1280.jpg", ScaleType.CENTER_CROP));
    adapter.add(new PicassoItem("http://24.media.tumblr.com/5f97f94756bf706bf41ac0dd37b585cf/tumblr_mzgzbdYBht1st5lhmo1_1280.jpg", ScaleType.CENTER_INSIDE));
    adapter.add(new PicassoItem("http://24.media.tumblr.com/6ddffd6a6036f61a1f2b1744bad77730/tumblr_mzgz9vJ1CK1st5lhmo1_1280.jpg", ScaleType.FIT_CENTER));
    adapter.add(new PicassoItem("http://25.media.tumblr.com/104330dfee76bb4713ea6c424a339b31/tumblr_mzgz92BX471st5lhmo1_1280.jpg", ScaleType.FIT_END));
    adapter.add(new PicassoItem("http://25.media.tumblr.com/c2aa498a075ab4b0c1b7c56120c140ab/tumblr_mzgz8arzYo1st5lhmo1_1280.jpg", ScaleType.FIT_START));
    adapter.add(new PicassoItem("http://25.media.tumblr.com/e886622da66651f4818f441e3120127d/tumblr_mzgz6yFP0u1st5lhmo1_1280.jpg", ScaleType.FIT_XY));
  }

  static class PicassoItem {
    final String mUrl;
    final ScaleType mScaleType;

    PicassoItem(String url, ScaleType scaleType) {
      this.mUrl = url;
      mScaleType = scaleType;
    }
  }

  class PicassoAdapter extends ArrayAdapter<PicassoItem> {
    private final LayoutInflater mInflater;

    public PicassoAdapter(Context context) {
      super(context, 0);
      mInflater = LayoutInflater.from(getContext());
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
      ViewGroup view;
      if (convertView == null) {
        view = (ViewGroup) mInflater.inflate(R.layout.picasso_item, parent, false);
      } else {
        view = (ViewGroup) convertView;
      }

      PicassoItem item = getItem(position);

      ImageView imageView = ((ImageView) view.findViewById(R.id.imageView1));

      Picasso.with(PicassoActivity.this)
          .load(item.mUrl)
          .fit()
          .transform(new Transformation() {
            float radius = 30;
            int border = 10;
            boolean oval = false;
            int color = Color.BLACK;

            @Override public Bitmap transform(Bitmap bitmap) {
              Bitmap transformed = RoundedDrawable.drawableToBitmap(
                  RoundedDrawable.fromBitmap(bitmap)
                      .setBorderColor(color)
                      .setBorderWidth(border)
                      .setCornerRadius(radius)
                      .setOval(oval));
              if (!bitmap.equals(transformed)) {
                bitmap.recycle();
              }
              return transformed;
            }

            @Override public String key() {
              return "rounded_radius_" + radius + "_border_" + border + "_color_" + color +  "_oval_" + oval ;
            }
          })
          .into(imageView);

      imageView.setScaleType(item.mScaleType);

      ((TextView) view.findViewById(R.id.textView3)).setText(item.mScaleType.toString());
      return view;
    }
  }
}
