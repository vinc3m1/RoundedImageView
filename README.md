RoundedImageView
================

A fast ImageView (and Drawable) that supports rounded corners based on the original [example from Romain Guy](http://www.curious-creature.org/2012/12/11/android-recipe-1-image-with-rounded-corners/)

There are many ways to create rounded corners, this is the fastest and best one that I know of because it:
* does **not** create a copy of the original bitmap
* does **not** use a clipPath which is not hardware accelerated and not anti-aliased.
* does **not** use setXfermode to clip the bitmap and draw twice to the canvas.

If you know of a better method, let me know and I'll implement it!

Also has proper support for:
* All ScaleTypes (borders are drawn at view edge, not bitmap edge).
* Borders
* Anti-aliasing
* Transparent backgrounds
* Hardware acceleration
* Support for TransitionDrawables (XML attrs only)


Usage
-----
Define in xml:

```xml
<com.makeramen.rounded.RoundedImageView
        xmlns:makeramen="http://schemas.android.com/apk/res/com.makeramen.rounded"
        android:id="@+id/imageView1"
        android:src="@drawable/photo1"
        android:scaleType="centerCrop"
        makeramen:corner_radius="30dip"
        makeramen:border="2dip"
        makeramen:border_color="#333333"
        makeramen:round_background="true" />
```

Or in code:

```java
RoundedImageView iv = new RoundedImageView(context);
iv.setScaleType(ScaleType.CENTER_CROP);
iv.setCornerRadius(10);
iv.setBorderWidth(2);
iv.setBorderColor(Color.DKGRAY);
iv.setRoundedBackground(true);
iv.setImageDrawable(drawable);
iv.setBackground(backgroundDrawable);
```

Known Issues
--------------------------------------
* Does not round images set by ```.setImageResource(int resId)```. Use ```BitmapFactory``` and ```setImageBitmap()``` instead.
* Programmatically setting attributes with TransitionDrawables not yet supported.
* Only tested support for BitmapDrawables and TransitionDrawables (with BitmapDrawables in them). Other types might work but may have unexpected behavior.
