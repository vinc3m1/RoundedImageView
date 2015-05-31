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

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.ArrayAdapter;
import com.makeramen.roundedimageview.example.RoundedFragment.ExampleType;

public class ExampleActivity extends FragmentActivity implements ActionBar.OnNavigationListener {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

    getActionBar().setListNavigationCallbacks(
        ArrayAdapter.createFromResource(
            getActionBar().getThemedContext(),
            R.array.action_list,
            android.R.layout.simple_spinner_dropdown_item),
        this);

    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
          .replace(android.R.id.content, new RoundedFragment())
          .commit();
    }

  }

  @Override public boolean onNavigationItemSelected(int itemPosition, long itemId) {

    Fragment newFragment;
    switch (itemPosition) {
      default:
      case 0:
        // bitmap
        newFragment = RoundedFragment.getInstance(ExampleType.DEFAULT);
        break;
      case 1:
        // oval
        newFragment = RoundedFragment.getInstance(ExampleType.OVAL);
        break;
      case 2:
        // select
        newFragment = RoundedFragment.getInstance(ExampleType.SELECT_CORNERS);
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
        .replace(android.R.id.content, newFragment)
        .commit();

    return true;
  }
}
