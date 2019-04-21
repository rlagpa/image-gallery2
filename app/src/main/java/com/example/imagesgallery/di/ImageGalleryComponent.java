package com.example.imagesgallery.di;

import com.example.imagesgallery.ui.ImageFragment;
import com.example.imagesgallery.ui.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * to use MainActivity
 * to use ImageFragment
 */

@Singleton
@Component(modules = ImageGalleryModule.class)
public interface ImageGalleryComponent {
    void inject(MainActivity activity);
    void inject(ImageFragment fragment);
}
