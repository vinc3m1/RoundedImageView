/*
* Copyright (C) 2014 Vincent Mi
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

package com.makeramen.roundedimageview.example;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;
import com.makeramen.roundedimageview.RoundedImageView;

public class RoundedFragment extends Fragment {

  static final String ARG_IS_OVAL = "is_oval";

  private boolean isOval = false;

  public static RoundedFragment getInstance(boolean isOval) {
    RoundedFragment f = new RoundedFragment();
    Bundle args = new Bundle();
    args.putBoolean(ARG_IS_OVAL, isOval);
    f.setArguments(args);
    return f;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (getArguments() != null) {
      isOval = getArguments().getBoolean(ARG_IS_OVAL);
    }
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_rounded, container, false);

    StreamAdapter adapter = new StreamAdapter(getActivity());

    adapter.add(new StreamItem(getActivity(),
        R.drawable.photo1, "Tufa at night", "Mono Lake, CA", ScaleType.CENTER));
    adapter.add(new StreamItem(getActivity(),
        R.drawable.photo2, "Starry night", "Lake Powell, AZ", ScaleType.CENTER_CROP));
    adapter.add(new StreamItem(getActivity(),
        R.drawable.photo3, "Racetrack playa", "Death Valley, CA", ScaleType.CENTER_INSIDE));
    adapter.add(new StreamItem(getActivity(),
        R.drawable.photo4, "Napali coast", "Kauai, HI", ScaleType.FIT_CENTER));
    adapter.add(new StreamItem(getActivity(),
        R.drawable.photo5, "Delicate Arch", "Arches, UT", ScaleType.FIT_END));
    adapter.add(new StreamItem(getActivity(),
        R.drawable.photo6, "Sierra sunset", "Lone Pine, CA", ScaleType.FIT_START));
    adapter.add(new StreamItem(getActivity(),
        R.drawable.photo7, "Majestic", "Grand Teton, WY", ScaleType.FIT_XY));
    adapter.add(new StreamItem(getActivity(),
        R.drawable.black_white_tile, "TileMode", "REPEAT", ScaleType.FIT_XY,
        Shader.TileMode.REPEAT));
    adapter.add(new StreamItem(getActivity(),
        R.drawable.black_white_tile, "TileMode", "CLAMP", ScaleType.FIT_XY,
        Shader.TileMode.CLAMP));
    adapter.add(new StreamItem(getActivity(),
        R.drawable.black_white_tile, "TileMode", "MIRROR", ScaleType.FIT_XY,
        Shader.TileMode.MIRROR));

    ((ListView) view.findViewById(R.id.main_list)).setAdapter(adapter);
    return view;
  }

  class StreamItem {
    final Bitmap mBitmap;
    final String mLine1;
    final String mLine2;
    final ScaleType mScaleType;
    final Shader.TileMode mTileMode;

    StreamItem(Context c, int resid, String line1, String line2, ScaleType scaleType) {
      this(c, resid, line1, line2, scaleType, Shader.TileMode.CLAMP);
    }

    StreamItem(Context c, int resid, String line1, String line2, ScaleType scaleType,
        Shader.TileMode tileMode) {
      mBitmap = BitmapFactory.decodeResource(c.getResources(), resid);
      mLine1 = line1;
      mLine2 = line2;
      mScaleType = scaleType;
      mTileMode = tileMode;
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
      ViewGroup view;
      if (convertView == null) {
        view = (ViewGroup) mInflater.inflate(R.layout.rounded_item, parent, false);
      } else {
        view = (ViewGroup) convertView;
      }

      StreamItem item = getItem(position);

      RoundedImageView iv = ((RoundedImageView) view.findViewById(R.id.imageView1));
      iv.setOval(isOval);
      iv.setImageBitmap(item.mBitmap);
      iv.setScaleType(item.mScaleType);
      iv.setTileModeX(item.mTileMode);
      iv.setTileModeY(item.mTileMode);
      ((TextView) view.findViewById(R.id.textView1)).setText(item.mLine1);
      ((TextView) view.findViewById(R.id.textView2)).setText(item.mLine2);
      ((TextView) view.findViewById(R.id.textView3)).setText(item.mScaleType.toString());

      return view;
    }
  }
}
