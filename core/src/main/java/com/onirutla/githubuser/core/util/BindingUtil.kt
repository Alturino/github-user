package com.onirutla.githubuser.core.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("loadImage")
fun loadImage(view: ImageView, any: Any?) {
    any?.let { GlideApp.with(view.context).load(it).into(view) }
}