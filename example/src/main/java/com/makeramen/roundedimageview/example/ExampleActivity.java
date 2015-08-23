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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class ExampleActivity extends AppCompatActivity
    implements AdapterView.OnItemSelectedListener {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.example_activity);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    Spinner navSpinner = (Spinner) findViewById(R.id.spinner_nav);

    navSpinner.setAdapter(ArrayAdapter.createFromResource(
        navSpinner.getContext(),
        R.array.action_list,
        android.R.layout.simple_spinner_dropdown_item));

    navSpinner.setOnItemSelectedListener(this);

    if (savedInstanceState == null) {
      navSpinner.setSelection(0);
    }
  }

  @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    Fragment newFragment;
    switch (position) {
      default:
      case 0:
        // bitmap
        newFragment = RoundedFragment.getInstance(RoundedFragment.ExampleType.DEFAULT);
        break;
      case 1:
        // oval
        newFragment = RoundedFragment.getInstance(RoundedFragment.ExampleType.OVAL);
        break;
      case 2:
        // select
        newFragment = RoundedFragment.getInstance(RoundedFragment.ExampleType.SELECT_CORNERS);
        break;
      case 3:
        // picasso
        newFragment = new PicassoFragment();
        break;
      case 4:
        // color
        newFragment = new ColorFragment();
        break;
    }

    getSupportFragmentManager().beginTransaction()
        .replace(R.id.fragment_container, newFragment)
        .commit();
  }

  @Override public void onNothingSelected(AdapterView<?> parent) { }
}
