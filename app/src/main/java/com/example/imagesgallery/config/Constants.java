package com.example.imagesgallery.config;



public interface Constants {
    interface Retro {
        int API_CALLBACK_THREADPOOL_SIZE = 10;//dafault(nullable)
    }

    interface EventBus {
        int EVENT_BUS_THREADPOOL_SIZE = 20; //dafault(MaxInteger)
    }

    interface Cache {
        int DISK_SIZE = 1024 * 1024 * 100;
    }

    ImageGalleryType CURRENT_TYPE = ImageGalleryType.GETTY;
}
