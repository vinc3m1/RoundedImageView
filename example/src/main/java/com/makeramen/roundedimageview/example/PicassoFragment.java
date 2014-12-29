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
import android.graphics.Color;
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
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class PicassoFragment extends Fragment {

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_rounded, container, false);

    PicassoAdapter adapter = new PicassoAdapter(getActivity());
    ((ListView) view.findViewById(R.id.main_list)).setAdapter(adapter);

    adapter.add(new PicassoItem("http://24.media.tumblr.com/2176464a507f8a34f09d58ee7fcf105a/tumblr_mzgzd79XMY1st5lhmo1_1280.jpg", ScaleType.CENTER));
    adapter.add(new PicassoItem("http://25.media.tumblr.com/af50758346e388e6e69f4c378c4f264f/tumblr_mzgzcdEDTL1st5lhmo1_1280.jpg", ScaleType.CENTER_CROP));
    adapter.add(new PicassoItem("http://24.media.tumblr.com/5f97f94756bf706bf41ac0dd37b585cf/tumblr_mzgzbdYBht1st5lhmo1_1280.jpg", ScaleType.CENTER_INSIDE));
    adapter.add(new PicassoItem("http://24.media.tumblr.com/6ddffd6a6036f61a1f2b1744bad77730/tumblr_mzgz9vJ1CK1st5lhmo1_1280.jpg", ScaleType.FIT_CENTER));
    adapter.add(new PicassoItem("http://25.media.tumblr.com/104330dfee76bb4713ea6c424a339b31/tumblr_mzgz92BX471st5lhmo1_1280.jpg", ScaleType.FIT_END));
    adapter.add(new PicassoItem("http://25.media.tumblr.com/c2aa498a075ab4b0c1b7c56120c140ab/tumblr_mzgz8arzYo1st5lhmo1_1280.jpg", ScaleType.FIT_START));
    adapter.add(new PicassoItem("http://25.media.tumblr.com/e886622da66651f4818f441e3120127d/tumblr_mzgz6yFP0u1st5lhmo1_1280.jpg", ScaleType.FIT_XY));

    return view;
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
    private final Transformation mTransformation;

    public PicassoAdapter(Context context) {
      super(context, 0);
      mInflater = LayoutInflater.from(getContext());
      mTransformation = new RoundedTransformationBuilder()
          .borderColor(Color.BLACK)
          .borderWidthDp(3)
          .cornerRadiusDp(30)
          .oval(false)
          .build();
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

      Picasso.with(getContext())
          .load(item.mUrl)
          .fit()
          .transform(mTransformation)
          .into(imageView);

      imageView.setScaleType(item.mScaleType);

      ((TextView) view.findViewById(R.id.textView3)).setText(item.mScaleType.toString());
      return view;
    }
  }
}
