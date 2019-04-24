package com.example.imagesgallery.service.image.cache;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

/**
 * cache에 대한 get set
 * order 1.memorycache 2.diskcache
 * 2가지 cache composite해서 동작
 */

public class CompositeImageCache implements ImageCache {

    private MemoryCache memoryCache = new MemoryCache();
    private DiskCache diskCache = new DiskCache();

    public CompositeImageCache(Context context) {
        memoryCache.init();
        diskCache.init(context);
    }

    public Bitmap get(String imageUrl) {
        Bitmap bitmap = null;

        if (memoryCache.isExistMemoryCache(imageUrl)) {
            bitmap = memoryCache.getBitmapFromMemCache(imageUrl);
        } else if (diskCache.isExistDiskCache(getKey(imageUrl))) {
            bitmap = diskCache.getBitmapFromDiskCache(getKey(imageUrl));
        }

        return bitmap;
    }

    private void addMemoryCache(String imageUrl, Bitmap bitmap) {
        memoryCache.addBitmapToMemoryCache(imageUrl, bitmap);
    }

    private void addDiskCache(String imageUrl, Bitmap bitmap) {
        diskCache.addBitmapToDiskCache(getKey(imageUrl), bitmap);
    }

    private String getKey(String imageUrl) {
        return String.valueOf(imageUrl.hashCode());
    }

    // TODO clear cache
//    public void clear() {
//        memoryCache.clearCache();
//        diskCache.clearCache();
//    }

    public void set(String imageUrl, Bitmap bitmap) {
        addMemoryCache(imageUrl, bitmap);
        addDiskCache(imageUrl, bitmap);
    }

    public void setAsync(String url, Bitmap bitmap) {
        new SetBitmapTask(url).execute(bitmap);
    }


    @SuppressLint("StaticFieldLeak") //outer is singletone
    class SetBitmapTask extends AsyncTask<Bitmap, Void, Void> {
        String url;

        SetBitmapTask(String url) {
            this.url = url;

        }

        @Override
        protected Void doInBackground(Bitmap... params) {
            set(url, params[0]);
            return null;
        }
    }
}
