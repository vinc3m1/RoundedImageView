RoundedImageView
================

A fast ImageView (and Drawable) that supports rounded corners (and ovals or circles) based on the original [example from Romain Guy](http://www.curious-creature.org/2012/12/11/android-recipe-1-image-with-rounded-corners/). RoundedImageView is a full superset of [CircleImageView](https://github.com/hdodenhof/CircleImageView) (which is actually just a subset based on this lib) with many more advanced features like support for ovals, rounded rectangles, ScaleTypes and TileModes.

![RoundedImageView screenshot](https://raw.github.com/makeramen/RoundedImageView/master/screenshot.png)
![RoundedImageView screenshot with ovals](https://raw.github.com/makeramen/RoundedImageView/master/screenshot-oval.png)

There are many ways to create rounded corners in android, but this is the fastest and best one that I know of because it:
* does **not** create a copy of the original bitmap
* does **not** use a clipPath which is not hardware accelerated and not anti-aliased.
* does **not** use setXfermode to clip the bitmap and draw twice to the canvas.

If you know of a better method, let me know (or even better open a pull request)!

Also has proper support for:
* Borders (with Colors and ColorStateLists)
* Ovals and Circles
* All `ScaleType`s
  * Borders are drawn at view edge, not bitmap edge
  * Except on edges where the bitmap is smaller than the view
  * Borders are **not** scaled up/down with the image (correct width and radius are maintained)
* Anti-aliasing
* Transparent backgrounds
* Hardware acceleration
* Support for LayerDrawables (including TransitionDrawables)
* TileModes for repeating drawables


Gradle
----
RoundedImageView is available in [Maven Central](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.makeramen%22%20AND%20a%3A%22roundedimageview%22).

Add the following to your `build.gradle` to use:
```
repositories {
    mavenCentral()
}

dependencies {
    compile 'com.makeramen:roundedimageview:1.5.0'
}
```

Usage
----
Define in xml:

```xml
<com.makeramen.roundedimageview.RoundedImageView
        xmlns:app="http://schemas.android.com/apk/res-auto"
        android:id="@+id/imageView1"
        android:src="@drawable/photo1"
        android:scaleType="fitCenter"
        app:riv_corner_radius="30dip"
        app:riv_border_width="2dip"
        app:riv_border_color="#333333"
        app:riv_mutate_background="true"
        app:riv_tile_mode="repeat"
        app:riv_oval="true" />
```

Or in code:

```java
RoundedImageView riv = new RoundedImageView(context);
riv.setScaleType(ScaleType.CENTER_CROP);
riv.setCornerRadius(10);
riv.setBorderWidth(2);
riv.setBorderColor(Color.DKGRAY);
riv.mutateBackground(true);
riv.setImageDrawable(drawable);
riv.setBackground(backgroundDrawable);
riv.setOval(true);
riv.setTileModeX(Shader.TileMode.REPEAT);
riv.setTileModeY(Shader.TileMode.REPEAT);
```

Or make a Transformation for Picasso:

```java
Transformation transformation = new RoundedTransformationBuilder()
          .borderColor(Color.BLACK)
          .borderWidthDp(3)
          .cornerRadiusDp(30)
          .oval(false)
          .build();

Picasso.with(context)
    .load(url)
    .fit()
    .transform(transformation)
    .into(imageView);
```

ChangeLog
----------

* **1.5.0**
    * [`Shader.TileMode`](http://developer.android.com/reference/android/graphics/Shader.TileMode.html) support

* **1.4.0**
    * initial ColorDrawable fix for Lollipop(5.0)
    * xml attributes are now namespaced and start with `riv_`
    * renamed methods `mutatesBackground()` and `mutateBackground(bool)`

* **1.3.1**
    * add support for About Libraries ([#57](https://github.com/vinc3m1/RoundedImageView/issues/57))
    * reset matrix for Robolectric support  ([#56](https://github.com/vinc3m1/RoundedImageView/issues/56))

* **1.3.0**
    * A new `RoundedTransformationBuilder` to help build Picasso `Transformation`s
    * slight API changes:
        * all dimensions are now set at `float`s. `int`s will be interpreted as dimension resource IDs
        * `round_background` is now `mutate_background`, and a `RoundedDrawable` will no longer be created for the background if `mutate_background` is false.

* **1.2.4**
    * add basic support for ColorDrawable (and other drawables with -1 intrinsic dimens)
    * implementation of the above is known to be buggy in many cases, pull requests welcome

* **1.2.3**
    * added rudimentary support for `setImageUri`. Performance of the function is probably poor and users should be cautious when using it.

* **1.2.2**
    * fix for incorrect radius on the image when there is a border
    * add a `toBitmap()` function for easier Picasso and Ion compatibility

* **1.2.1**
    * default scaleType now FIT_CENTER (and never null) to match Android ([#27](https://github.com/vinc3m1/RoundedImageView/issues/27))

* **1.2.0**
    * add `setDither` and `setFilterBitmap` method support on RoundedDrawable for tuning bitmap scaling quality
    * improved performance for `setImageResource`
    * RoundedDrawable constructor is now public
    * Fixed bug where artifact was downloading `aar.asc` file instead of aar. You no longer need to have `@aar` specified in the dependency

* **1.1.0**
    * LayerDrawable support (needs testing!)
    * Refactored api to support chaining and remove repetitive code

* **1.0.0**
    * Initial release to maven central
    * Programmatically setting attributes with TransitionDrawables not supported.



[![Bitdeli Badge](https://d2weczhvl823v0.cloudfront.net/vinc3m1/roundedimageview/trend.png)](https://bitdeli.com/free "Bitdeli Badge")

