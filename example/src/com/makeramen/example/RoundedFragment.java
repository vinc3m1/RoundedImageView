package com.makeramen.example;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;

public class RoundedFragment extends Fragment {

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_rounded, container, false);

    StreamAdapter adapter = new StreamAdapter(getActivity());

    adapter.add(new StreamItem(getActivity(), R.drawable.photo1, "Tufa at night", "Mono Lake, CA",
        ScaleType.CENTER));
    adapter.add(new StreamItem(getActivity(), R.drawable.photo2, "Starry night", "Lake Powell, AZ",
        ScaleType.CENTER_CROP));
    adapter.add(
        new StreamItem(getActivity(), R.drawable.photo3, "Racetrack playa", "Death Valley, CA",
            ScaleType.CENTER_INSIDE));
    adapter.add(
        new StreamItem(getActivity(), R.drawable.photo4, "Napali coast", "Kauai, HI",
            ScaleType.FIT_CENTER));
    adapter.add(
        new StreamItem(getActivity(), R.drawable.photo5, "Delicate Arch", "Arches, UT",
            ScaleType.FIT_END));
    adapter.add(new StreamItem(getActivity(), R.drawable.photo6, "Sierra sunset", "Lone Pine, CA",
        ScaleType.FIT_START));
    adapter.add(
        new StreamItem(getActivity(), R.drawable.photo7, "Majestic", "Grand Teton, WY",
            ScaleType.FIT_XY));

    ((ListView) view.findViewById(R.id.main_list)).setAdapter(adapter);
    return view;
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
      ViewGroup view;
      if (convertView == null) {
        view = (ViewGroup) mInflater.inflate(R.layout.rounded_item, parent, false);
      } else {
        view = (ViewGroup) convertView;
      }

      StreamItem item = getItem(position);

      ((ImageView) view.findViewById(R.id.imageView1)).setImageBitmap(item.mBitmap);
      ((ImageView) view.findViewById(R.id.imageView1)).setScaleType(item.mScaleType);
      ((TextView) view.findViewById(R.id.textView1)).setText(item.mLine1);
      ((TextView) view.findViewById(R.id.textView2)).setText(item.mLine2);
      ((TextView) view.findViewById(R.id.textView3)).setText(item.mScaleType.toString());
      return view;
    }
  }
}
