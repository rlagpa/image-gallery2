package com.example.imagesgallery.service;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.Executors;

public enum Events {
    BUS;

    private EventBus bus;

    Events() {
        bus = EventBus.builder().executorService(Executors.newFixedThreadPool(20)).build();
    }

    public EventBus get() {
        return bus;
    }
}
