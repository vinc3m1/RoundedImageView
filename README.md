RoundedImageView
================

A fast ImageView (and Drawable) that supports rounded corners based on the original [example from Romain Guy](http://www.curious-creature.org/2012/12/11/android-recipe-1-image-with-rounded-corners/)

Has proper support for:
* Borders
* Anti-aliasing
* Transparent backgrounds
* Hardware acceleration
* (Coming Soon) proper ScaleType support


Usage
-----
Define in xml:

```xml
<com.makeramen.rounded.RoundedImageView
        xmlns:makeramen="http://schemas.android.com/apk/res/com.makeramen.rounded"
        android:id="@+id/imageView1"
        android:src="@drawable/photo1"
        android:scaleType="fitXY"
        makeramen:corner_radius="30dip"
        makeramen:border="2dip"
        makeramen:border_color="#333333"
        makeramen:round_background="true" />
```

More programmatic hooks on the way.


To Do
-----

* Add different ScaleTypes
* Support for ColorDrawables
* Add programmatic hooks to XML properties

Known Issues (no  quick fix available)
--------------------------------------

* Does not round images set by ```.setImageResource(int resId)```. Use ```BitmapFactory``` and ```setImageBitmap()``` instead.
