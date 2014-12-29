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
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;

public class ColorFragment extends Fragment {

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_rounded, container, false);

    StreamAdapter adapter = new StreamAdapter(getActivity());
    ((ListView) view.findViewById(R.id.main_list)).setAdapter(adapter);

    adapter.add(
        new ColorItem(android.R.color.darker_gray, "Tufa at night", "Mono Lake, CA",
            ScaleType.CENTER));
    adapter.add(
        new ColorItem(android.R.color.holo_orange_dark, "Starry night", "Lake Powell, AZ",
            ScaleType.CENTER_CROP));
    adapter.add(
        new ColorItem(android.R.color.holo_blue_dark, "Racetrack playa", "Death Valley, CA",
            ScaleType.CENTER_INSIDE));
    adapter.add(
        new ColorItem(android.R.color.holo_green_dark, "Napali coast", "Kauai, HI",
            ScaleType.FIT_CENTER));
    adapter.add(
        new ColorItem(android.R.color.holo_red_dark, "Delicate Arch", "Arches, UT",
            ScaleType.FIT_END));
    adapter.add(
        new ColorItem(android.R.color.holo_purple, "Sierra sunset", "Lone Pine, CA",
            ScaleType.FIT_START));
    adapter.add(
        new ColorItem(android.R.color.white, "Majestic", "Grand Teton, WY",
            ScaleType.FIT_XY));

    return view;
  }

  class ColorItem {
    final int mResId;
    final String mLine1;
    final String mLine2;
    final ScaleType mScaleType;

    ColorItem(int resid, String line1, String line2, ScaleType scaleType) {
      mResId = resid;
      mLine1 = line1;
      mLine2 = line2;
      mScaleType = scaleType;
    }
  }

  class StreamAdapter extends ArrayAdapter<ColorItem> {
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

      ColorItem item = getItem(position);

      ((ImageView) view.findViewById(R.id.imageView1)).setImageResource(item.mResId);
      ((ImageView) view.findViewById(R.id.imageView1)).setScaleType(item.mScaleType);
      ((TextView) view.findViewById(R.id.textView1)).setText(item.mLine1);
      ((TextView) view.findViewById(R.id.textView2)).setText(item.mLine2);
      ((TextView) view.findViewById(R.id.textView3)).setText(item.mScaleType.toString());
      return view;
    }
  }
}
