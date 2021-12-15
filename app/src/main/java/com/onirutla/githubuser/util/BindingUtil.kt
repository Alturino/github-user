package com.onirutla.githubuser.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("loadImage")
fun loadImage(view: ImageView, any: Any?) {
    if (any != null) {
        GlideApp.with(view.context).load(any).into(view)
    }
}