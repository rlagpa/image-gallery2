package com.example.imagesgallery.model;

import android.graphics.Bitmap;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BitmapDto { //user for loading image
    /**
     *  RecyclerView position
     **/
    int position;
    /**
     *  url of image(use for cache key)
     **/
    String url;
    /**
     *  bitmap of image
     **/
    Bitmap bitmap;

}
