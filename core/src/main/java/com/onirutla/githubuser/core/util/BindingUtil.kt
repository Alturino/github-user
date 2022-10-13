package com.onirutla.githubuser.core.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.imageLoader
import coil.request.ImageRequest

@BindingAdapter("loadImage")
fun loadImage(view: ImageView, any: Any?) {
    any?.let {
        val context = view.context
        val request = ImageRequest.Builder(context)
            .data(it)
            .target(view)
            .build()
        context.imageLoader.enqueue(request)
    }
}