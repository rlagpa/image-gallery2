package com.example.imagesgallery.service.image.cache;


import android.graphics.Bitmap;

/**
 * cache 이미지를 등록하거나 가져오기 위한 interface(CompositionImageCache 에서 사용)
 */

public interface ImageCache {
    Bitmap get(String imageUri);
    void set(String imageUri, Bitmap bitmap);
}
