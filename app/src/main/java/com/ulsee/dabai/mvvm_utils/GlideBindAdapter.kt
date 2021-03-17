package com.ulsee.dabai.mvvm_utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide


// app:imageURL="@{viewmodel.imageURL}"
@BindingAdapter("bind:imageURL")
fun loadImageURL(iv: ImageView, url: String?) {
    Glide.with(iv.context).load(url).into(iv);
}