package com.example.imagesgallery.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Builder
@Getter
public class ImageDto {
    @Setter
    private int position;
    /**
     * url of image
     */
    @NonNull private String url;
    /**
     * linkurl of image(use for click)
     */
    @NonNull private String linkUrl;

    public int width;
    public int height;
}
