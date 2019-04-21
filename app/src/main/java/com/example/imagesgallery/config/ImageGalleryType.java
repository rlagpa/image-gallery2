package com.example.imagesgallery.config;

import com.example.imagesgallery.service.parser.GettyRootHtmlParser;
import com.example.imagesgallery.service.parser.RootHtmlParser;

/**
 * 각 image site에 맞는 정보 등록
 */

public enum ImageGalleryType {
    GETTY("https://www.gettyimagesgallery.com/", new GettyRootHtmlParser());

    String baseUrl;
    RootHtmlParser parser;

    ImageGalleryType(String baseUrl, RootHtmlParser parser) {
        this.baseUrl = baseUrl;
        this.parser = parser;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public RootHtmlParser getParser() {
        return parser;
    }
}
