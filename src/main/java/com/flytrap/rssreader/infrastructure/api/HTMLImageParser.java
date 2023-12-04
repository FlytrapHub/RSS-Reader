package com.flytrap.rssreader.infrastructure.api;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

@Component
public class HTMLImageParser {

    private static final String IMG_TAG_NAME = "img";
    private static final String SRC_ATTRIBUTE_NAME = "src";

    public String extractImageUrl(String html) {
        Document doc = Jsoup.parse(html);
        Element imgElement = doc.select(IMG_TAG_NAME).first();

        if (imgElement != null) {
            return imgElement.attr(SRC_ATTRIBUTE_NAME);
        } else {
            return null;
        }
    }
}
