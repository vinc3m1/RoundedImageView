package com.makeramen.roundedimageview;

import java.util.EnumSet;

public enum Corners {

  ALL(EnumSet.of(Corner.TOP_LEFT, Corner.TOP_RIGHT, Corner.BOTTOM_LEFT, Corner.BOTTOM_RIGHT)),
  TOP_LEFT(EnumSet.of(Corner.TOP_LEFT)),
  TOP_RIGHT(EnumSet.of(Corner.TOP_RIGHT)),
  BOTTOM_LEFT(EnumSet.of(Corner.BOTTOM_LEFT)),
  BOTTOM_RIGHT(EnumSet.of(Corner.BOTTOM_RIGHT)),
  TOP(EnumSet.of(Corner.TOP_LEFT, Corner.TOP_RIGHT)),
  BOTTOM(EnumSet.of(Corner.BOTTOM_LEFT, Corner.BOTTOM_RIGHT)),
  LEFT(EnumSet.of(Corner.TOP_LEFT, Corner.BOTTOM_LEFT)),
  RIGHT(EnumSet.of(Corner.TOP_RIGHT, Corner.BOTTOM_RIGHT)),
  OTHER_TOP_LEFT(EnumSet.of(Corner.TOP_RIGHT, Corner.BOTTOM_LEFT, Corner.BOTTOM_RIGHT)),
  OTHER_TOP_RIGHT(EnumSet.of(Corner.TOP_LEFT, Corner.BOTTOM_LEFT, Corner.BOTTOM_RIGHT)),
  OTHER_BOTTOM_LEFT(EnumSet.of(Corner.TOP_LEFT, Corner.TOP_RIGHT, Corner.BOTTOM_RIGHT)),
  OTHER_BOTTOM_RIGHT(EnumSet.of(Corner.TOP_LEFT, Corner.TOP_RIGHT, Corner.BOTTOM_LEFT)),
  DIAGONAL_FROM_TOP_LEFT(EnumSet.of(Corner.TOP_LEFT, Corner.BOTTOM_RIGHT)),
  DIAGONAL_FROM_TOP_RIGHT(EnumSet.of(Corner.TOP_RIGHT, Corner.BOTTOM_LEFT));

  public final EnumSet<Corner> corners;

  Corners(EnumSet<Corner> corners) {
    this.corners = corners;
  }

  /**
   * @param radius radius to apply
   * @return radius of top left corner, or zero if corner must not be rounded.
   */
  public float topLeft(float radius) {
    return getRadius(Corner.TOP_LEFT, radius);
  }

  /**
   * @param radius radius to apply
   * @return radius of top right corner, or zero if corner must not be rounded.
   */
  public float topRight(float radius) {
    return getRadius(Corner.TOP_RIGHT, radius);
  }

  /**
   * @param radius radius to apply
   * @return radius of bottom left corner, or zero if corner must not be rounded.
   */
  public float bottomLeft(float radius) {
    return getRadius(Corner.BOTTOM_LEFT, radius);
  }

  /**
   * @param radius radius to apply
   * @return radius of bottom right corner, or zero if corner must not be rounded.
   */
  public float bottomRight(float radius) {
    return getRadius(Corner.BOTTOM_RIGHT, radius);
  }

  /**
   * @param radius to apply
   * @return array, containing all corners radii, or zero, if some corners must not be rounded
   */
  public float[] getAll(final float radius) {
    return getAll(radius, radius, radius, radius);
  }

  /**
   * @param topLeft     corner radius
   * @param topRight    corner radius
   * @param bottomLeft  corner radius
   * @param bottomRight corner radius
   * @return array, containing all corners radii, or zero, if some corners must not be rounded
   */
  public float[] getAll(final float topLeft, final float topRight, final float bottomLeft, final float bottomRight) {
    return applyTo(new float[4], topLeft, topRight, bottomLeft, bottomRight);
  }

  /**
   * @param allCorners array to apply radii to
   * @param radius to apply
   * @return array, containing all corners radii, or zero, if some corners must not be rounded
   */
  public float[] applyTo(final float[] allCorners, final float radius) {
    return applyTo(allCorners, radius, radius, radius, radius);
  }

  /**
   * @param allCorners  array of radii to apply to
   * @param topLeft     corner radius
   * @param topRight    corner radius
   * @param bottomLeft  corner radius
   * @param bottomRight corner radius
   * @return same array
   */
  public float[] applyTo(final float[] allCorners, final float topLeft, final float topRight, final float bottomLeft, final float bottomRight) {
    allCorners[Corner.TOP_LEFT.index] = topLeft(topLeft);
    allCorners[Corner.TOP_RIGHT.index] = topRight(topRight);
    allCorners[Corner.BOTTOM_LEFT.index] = bottomLeft(bottomLeft);
    allCorners[Corner.BOTTOM_RIGHT.index] = bottomRight(bottomRight);
    return allCorners;
  }

  private float getRadius(final Corner corner, final float radius) {
    return corners.contains(corner) ? radius : 0;
  }

  /**
   *
   * @param allCorners  array to check radii equality with
   * @param topLeft     corner radius
   * @param topRight    corner radius
   * @param bottomLeft  corner radius
   * @param bottomRight corner radius
   * @return is radii in provided array same as provided in parameters
   */
  public static boolean isSameRadii(final float[] allCorners,
                                    final float topLeft,
                                    final float topRight,
                                    final float bottomLeft,
                                    final float bottomRight) {
    return allCorners[Corner.TOP_LEFT.index] == topLeft
        && allCorners[Corner.TOP_RIGHT.index] == topRight
        && allCorners[Corner.BOTTOM_RIGHT.index] == bottomRight
        && allCorners[Corner.BOTTOM_LEFT.index] == bottomLeft;
  }
}
