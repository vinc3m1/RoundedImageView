package com.makeramen.roundedimageview;

import android.graphics.Bitmap;

import com.squareup.picasso.Transformation;

public class PicassoRoundedTransformationBuilder {

  private RoundedTransformationBuilder mBuilder;

  public PicassoRoundedTransformationBuilder(final RoundedTransformationBuilder builder) {
    mBuilder = builder.copy();
  }

  /**
   * Creates a {@link Transformation} for use with picasso.
   *
   * @return the {@link Transformation}
   */
  public Transformation build() {
    return new Transformation() {
      @Override
      public Bitmap transform(Bitmap source) {
        Bitmap transformed = mBuilder.transform(source);
        if (!source.equals(transformed)) {
          source.recycle();
        }
        return transformed;
      }

      @Override
      public String key() {
        return mBuilder.getId();
      }
    };
  }

}
