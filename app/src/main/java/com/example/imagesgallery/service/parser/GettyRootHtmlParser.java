package com.example.imagesgallery.service.parser;

import com.example.imagesgallery.model.ImageDto;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * getty images gallery site parser
 */

public class GettyRootHtmlParser implements RootHtmlParser {
    @Override
    public List<ImageDto> parse(InputStream in, String hostUrl) {
        Document document;
        try {
            document = Jsoup.parse(in, "UTF-8", hostUrl);
        }catch (Exception e) {
            return null;
        }

        //FIXME use stream
        List<ImageDto> imageList = new ArrayList<>();
        Elements group = document.getElementsByClass("grid-item image-item col-md-4");
        for(Element element : group) {
            imageList.add(ImageDto.builder()
                    .url(element.getElementsByClass("jq-lazy").attr("data-src"))
                    .linkUrl(element.select("a").attr("href"))
                    .build());
        }

        return imageList;
    }
}
