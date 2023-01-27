# [ARCHIVED] RoundedImageView is no longer actively maintained as there are better alternatives available

RoundedImageView was originally made with a primary goal of memory and rendering performance, and specifically only worked well on Bitmaps. It had some very poorly performing hacks built in for non-bitmaps, along with a lot of hard to maintain synchronization of ScaleTypes and sizing between the ImageView and the Drawable itself. 

RoundedImageView has also never supported image loaders like Picasso and Glide well, especially when they leverage TransitionDrawables for fade effects. These use cases are always best served by using the respective library's own Transformation APIs for image manipulation inside the async pipelines where the performance impact is less apparent.

As the ImageView and Drawable APIs evolved to support even more features like tinting, and Android's hardware acceleration support improved, the need for a library like RoundedImageView has dwindled while the maintenance cost remained. Finally with the inclusion of [RoundedBitmapDrawable](https://developer.android.com/reference/kotlin/androidx/core/graphics/drawable/RoundedBitmapDrawable) in androidx which has a much simpler (and better IMO) API, I have recommended that as the best solution for everyone considering RoundedImageView.


## Here are my recommended alternatives:

### 1. For rounding bitmaps bundled in your APK (not loaded from internet or disk) use [RoundedBitmapDrawable](https://developer.android.com/reference/kotlin/androidx/core/graphics/drawable/RoundedBitmapDrawable)

RoundedBitmapDrawable avoids the many pitfalls of RoundedImageView by:
1. Working only on Bitmaps and BitmapDrawables which is what RoundedImageView was primrarily built for.
2. Decoupling the Drawable from the ImageView, which could have different size and ScaleType constraints and was always complicated to synchronize in RoundedImageView.
3. Decoupling the Drawable from other Drawable wrappers like [TransitionDrawable](https://developer.android.com/reference/android/graphics/drawable/TransitionDrawable) and other [LayerDrawable](https://developer.android.com/reference/android/graphics/drawable/LayerDrawable) and [DrawableContainer](https://developer.android.com/reference/android/graphics/drawable/DrawableContainer) types. This was always hard to manage in RoundedImageView because it was hard to guess the intent of the author whether

### 2. For loading from network or disk, use the library's transformation API, for example [wasabeef/glide-transformations](https://github.com/wasabeef/glide-transformations) can round images loaded by [Glide](https://github.com/bumptech/glide), and [wasabeef/picasso-transformations](https://github.com/wasabeef/picasso-transformations) can round images loaded by [Picasso](https://github.com/square/picasso).

### 3. For vector drawables and other non-Bitmap drawables, modify the vector or drawable to include rounding for best performance.

RoundedImageView would try its best to rasterize these to a Bitmap and round them, at a rather high cost to memory (creating an extra bitmap) and fidelity (scaling rasterized bitmaps leads to artifacts). While rasterizing these drawables technically works, the resulting performance and quality impact is in direct contrast with the original goals of RoundedImageView around memory efficiency and image quality and probably should have been left unsupported completely.

---

# RoundedImageView

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.makeramen/roundedimageview/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.makeramen/roundedimageview)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-RoundedImageView-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/680)

A fast ImageView (and Drawable) that supports rounded corners (and ovals or circles) based on the original [example from Romain Guy](http://www.curious-creature.org/2012/12/11/android-recipe-1-image-with-rounded-corners/). It supports many additional features including ovals, rounded rectangles, ScaleTypes and TileModes.

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

## Known Issues

- VectorDrawables are **not** supported. This library is designed for BitmapDrawables only. Other drawables will likely fail or cause high memory usage. 
- ColorDrawables are poorly supported, use your own rounded VectorDrawables instead if you want less memory pressure.
- Glide transforms are **not** supported, please use [wasabeef/glide-transformations](https://github.com/wasabeef/glide-transformations) if you want to round images loaded from Glide.

## Gradle

RoundedImageView is available in [Maven Central](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.makeramen%22%20AND%20a%3A%22roundedimageview%22).

Add the following to your `build.gradle` to use:
```
repositories {
    mavenCentral()
}

dependencies {
    implementation 'com.makeramen:roundedimageview:2.3.0'
}
```


## Usage

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

### Picasso

To make a Transformation for Picasso:

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

## Changelog

see [Releases](https://github.com/vinc3m1/RoundedImageView/releases)




## License

    Copyright 2017 Vincent Mi

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
