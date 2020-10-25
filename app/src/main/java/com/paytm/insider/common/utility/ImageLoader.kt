package com.paytm.insider.common.utility

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

object ImageLoader {

    fun loadImageFromUrl(url: String?, imageView: ImageView) {
        Glide.with(imageView.context)
            .load(url)
            .apply(
                RequestOptions.priorityOf(Priority.IMMEDIATE)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .skipMemoryCache(true)
            )
            .into(imageView)
    }
}