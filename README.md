RoundedImageView
================

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.makeramen/roundedimageview/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.makeramen/roundedimageview)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-RoundedImageView-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/680)

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
    compile 'com.makeramen:roundedimageview:2.1.3'
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
riv.setCornerRadius((float) 10);
riv.setBorderWidth((float) 2);
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

Changelog
----------
see [Releases](https://github.com/vinc3m1/RoundedImageView/releases)
