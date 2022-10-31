package com.alexander.rickmorty.ui

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("loadImageCircleCrop")
fun bindImageCircleCrop(view: ImageView, url: String?) {
    url.let {
        Glide.with(view)
                .load(it)
                .circleCrop()
                .into(view)
    }
}

@BindingAdapter("loadImage")
fun bindImage(view: ImageView, url: String?) {
    url.let {
        Glide.with(view)
                .load(it)
                .into(view)
    }
}
