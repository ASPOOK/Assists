package com.aspook.assists.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.module.GlideModule;

/**
 * custom config for Glide
 * <p>
 * Created by ASPOOK on 17/7/20.
 */

public class AssistsGlideModule implements GlideModule {
    private static final int DISK_CACHE_SIZE = 100 * 1024 * 1024;

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // PATH = packageName/cache/glide_cache; SIZE = MAX_CACHE_DISK_SIZE
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, "glide_cache", DISK_CACHE_SIZE));
        builder.setDecodeFormat(DecodeFormat.PREFER_RGB_565);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
