package com.example.imagesgallery.service;

import com.example.imagesgallery.config.Constants;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.Executors;

public enum Events {
    BUS;

    private EventBus bus;

    Events() {
        bus = EventBus.builder().executorService(Executors.newFixedThreadPool(Constants.EventBus.EVENT_BUS_THREADPOOL_SIZE)).build();
    }

    public void post(Object event) {
        bus.post(event);
    }

    public void regist(Object subscriber) {
        bus.register(subscriber);
    }

    public void unregist(Object subscriber) {
        bus.unregister(subscriber);
    }
}
