package com.example.imagesgallery.service.parser;

import com.example.imagesgallery.model.ImageDto;

import java.io.InputStream;
import java.util.List;

/**
 * image galley site parser
 */

public interface RootHtmlParser {
    List<ImageDto> parse(InputStream in, String hostUrl);
}
