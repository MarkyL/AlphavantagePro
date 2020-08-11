package com.mark.alphavantage.utils

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.module.AppGlideModule
import com.mark.alphavantage.core.Constants

@GlideModule
class GlideConfiguration : AppGlideModule() {
    private val CACHE_SIZE = 25 * 1024 * 1024 //25MB

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        builder.setDiskCache(InternalCacheDiskCacheFactory(context, Constants.CACHE_IMAGE_SIZE))
    }

    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}