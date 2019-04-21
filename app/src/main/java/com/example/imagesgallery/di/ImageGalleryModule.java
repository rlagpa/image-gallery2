package com.example.imagesgallery.di;

import android.content.Context;

import com.example.imagesgallery.service.image.ImageDownloadService;
import com.example.imagesgallery.service.image.ImageListService;
import com.example.imagesgallery.service.image.ImageService;
import com.example.imagesgallery.service.image.cache.CompositeImageCache;
import com.example.imagesgallery.ui.ImageRecyclerAdapter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * define inject information
 */

@Module
public class ImageGalleryModule {

    private Context context;

    public ImageGalleryModule(Context context) {
        this.context = context;
    }

    @Provides
    ImageRecyclerAdapter imageRecyclerAdapter() {
        return new ImageRecyclerAdapter(context);
    }

    @Provides @Singleton
    ImageService imageService(CompositeImageCache cache,
                              ImageListService listService,
                              ImageDownloadService downloadService) {
        return new ImageService(cache, listService, downloadService);
    }

    @Provides @Singleton
    CompositeImageCache compositeImageCache() {
        return new CompositeImageCache(context);
    }

    @Provides @Singleton
    ImageListService imageListService() {
        return new ImageListService();
    }

    @Provides @Singleton
    ImageDownloadService imageDownloadService(CompositeImageCache cache) {
        return new ImageDownloadService(cache);
    }

}
