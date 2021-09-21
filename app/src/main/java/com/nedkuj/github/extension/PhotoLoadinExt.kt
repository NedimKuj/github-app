package com.nedkuj.github.extension

import android.graphics.Bitmap
import android.util.Size
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation

fun ImageView.loadFromUrl(url: String, size: Size?, placeholder: Int) {
    loadFromUrl(url, size, placeholder, Bitmap.Config.ARGB_8888)
}

fun ImageView.loadFromUrl(
        url: String, size: Size?, @DrawableRes placeholder: Int = -1,
        config: Bitmap.Config = Bitmap.Config.ARGB_8888
) {
    loadImageUrl(url, this, size, placeholder, config, null, null)
}

fun loadImageUrl(
        url: String,
        image: ImageView,
        size: Size?, @DrawableRes placeholder: Int,
        config: Bitmap.Config,
        transformation: Any?,
        cacheStrategy: DiskCacheStrategy? = DiskCacheStrategy.AUTOMATIC
) {
    val glide = Glide.with(image.context)
            .load(url)
    if (size != null && size.width != -1 && size.height != -1)
        glide.submit(size.width, size.height)
    if (placeholder != -1)
        glide.placeholder(placeholder)
    if (config == Bitmap.Config.RGB_565)
        glide.format(DecodeFormat.PREFER_RGB_565)
    else
        glide.format(DecodeFormat.PREFER_ARGB_8888)
    if (transformation is BitmapTransformation) {
        glide.transform(transformation).into(image)
    } else {
        if (cacheStrategy != null)
            glide.centerCrop().diskCacheStrategy(cacheStrategy).into(image)
        else glide.centerCrop().into(image)
    }
}