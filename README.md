RoundedImageView
================

A fast ImageView (and Drawable) that supports rounded corners based on the original [example from Romain Guy](http://www.curious-creature.org/2012/12/11/android-recipe-1-image-with-rounded-corners/)

Usage
-----
Coming soon... see source for now.


To Do
-----

* Add different ScaleTypes
* Support for ColorDrawables
* Add programatic hooks to XML properties

Known Issues (no  quick fix available)
--------------------------------------

* Does not round images set by ```.setImageResource(int resId)```. Use ```BitmapFactory``` and ```setImageBitmap()``` instead
