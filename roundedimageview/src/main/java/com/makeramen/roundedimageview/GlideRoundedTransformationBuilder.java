package com.makeramen.roundedimageview;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

public class GlideRoundedTransformationBuilder {

  private RoundedTransformationBuilder mBuilder;

  public GlideRoundedTransformationBuilder(final RoundedTransformationBuilder builder) {
    mBuilder = builder.copy();
  }

  /**
   * Creates a {@link BitmapTransformation} for use with Glide.
   *
   * @return the {@link BitmapTransformation}
   */
  public BitmapTransformation build(final Context context) {
    return new BitmapTransformation(context) {

      @Override
      protected Bitmap transform(final BitmapPool pool, final Bitmap toTransform, final int outWidth, final int outHeight) {
        return mBuilder.transform(toTransform);
      }

      @Override
      public String getId() {
        return mBuilder.getId();
      }
    };
  }

}
