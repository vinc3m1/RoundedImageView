package com.makeramen.roundedimageview;

public enum Corner {
  TOP_LEFT(0),
  TOP_RIGHT(1),
  BOTTOM_LEFT(2),
  BOTTOM_RIGHT(3);

  public int index;

  Corner(final int index) {
    this.index = index;
  }
}
