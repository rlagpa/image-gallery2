package com.example.imagesgallery.service.image;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.example.imagesgallery.model.BitmapDto;
import com.example.imagesgallery.model.ImageDto;
import com.example.imagesgallery.service.Events;
import com.example.imagesgallery.service.image.cache.CompositeImageCache;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * image listing, downloading, cacheing에 대한 파사드 레이어
 * 이미지 관련된 작업
 * 1. 이미지 리스트를 만들기 위한 작업
 * 2. 이미지를 로딩하기 위한 요청(compositecache or url)
 */

public class ImageService {

    private CompositeImageCache cache;
    private ImageListService imageListService;
    private ImageDownloadService downloadService;

    public ImageService(CompositeImageCache cache, ImageListService imageListService, ImageDownloadService downloadService) {
        this.cache = cache;
        this.imageListService = imageListService;
        this.downloadService = downloadService;
    }

    public void requestImages() {
        imageListService.request();
    }

    //load image(check cache and download image(set cache))
    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void load(ImageDto image) {
        //check for exist cache
        Bitmap bitmap = cache.get(image.getUrl());

        if (bitmap == null) {
            downloadService.download(image); //download for url
        } else {
            BitmapDto target = BitmapDto.builder()
                    .position(image.getPosition())
                    .url(image.getUrl())
                    .bitmap(bitmap)
                    .build();

            Events.BUS.post(target);
        }
    }

    // set cache on bitmap load event
//    @Subscribe(threadMode = ThreadMode.BACKGROUND)
//    public void onBitmapLoad(@NonNull BitmapDto dto) {
//        cache.set(dto.getUrl(), dto.getBitmap());
//    }
}
