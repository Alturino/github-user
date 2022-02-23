package com.onirutla.githubuser.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("loadImage")
fun loadImage(view: ImageView, fileName: String) {
    fileName.let {
        val context = view.context
        GlideApp.with(view)
            .load(context.resources.getIdentifier(it, "drawable", context.packageName))
            .into(view)
    }
}
