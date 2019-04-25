package com.example.imagesgallery.di;

import com.example.imagesgallery.ui.ImageFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * to use MainActivity
 * to use ImageFragment
 */

@Singleton
@Component(modules = ImageGalleryModule.class)
public interface ImageGalleryComponent {
    void inject(ImageFragment fragment);
}
