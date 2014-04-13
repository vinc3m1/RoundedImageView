package com.makeramen.example;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class ExampleActivity extends Activity implements ActionBar.OnNavigationListener {

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
      getFragmentManager().beginTransaction()
          .replace(android.R.id.content, new RoundedFragment())
          .commit();
    }

  }

  @Override public boolean onNavigationItemSelected(int itemPosition, long itemId) {

    Fragment newFragment;
    switch (itemPosition) {
      default:
      case 0:
        newFragment = RoundedFragment.getInstance(false);
        break;
      case 1:
        newFragment = RoundedFragment.getInstance(true);
        break;
      case 2:
        newFragment = new PicassoFragment();
        break;
      case 3:
        newFragment = new ColorFragment();
        break;
    }

    getFragmentManager().beginTransaction()
        .replace(android.R.id.content, newFragment)
        .commit();

    return true;
  }
}
