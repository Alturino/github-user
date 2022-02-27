package com.onirutla.githubuser.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("loadImage")
fun loadImage(view: ImageView, any: Any?) {
    any?.let { GlideApp.with(view).load(it).into(view) }
}
