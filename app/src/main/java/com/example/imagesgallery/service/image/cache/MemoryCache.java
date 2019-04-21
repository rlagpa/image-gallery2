package com.example.imagesgallery.service.image.cache;

import android.graphics.Bitmap;
import android.util.LruCache;



class MemoryCache {
    private LruCache<String, Bitmap> mMemoryCache;

    void init() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    boolean isExistMemoryCache(String key) {
        return getBitmapFromMemCache(key) != null;
    }

    void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    void clearCache() {
        mMemoryCache.evictAll();
    }
}
