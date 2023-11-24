package com.flytrap.rssreader.infrastructure.api;

import com.flytrap.rssreader.domain.subscribe.BlogPlatform;
import com.flytrap.rssreader.infrastructure.api.dto.RssItemTagName;
import com.flytrap.rssreader.presentation.dto.RssFeedData;
import com.flytrap.rssreader.presentation.dto.SubscribeRequest.CreateRequest;
import java.io.IOException;
import java.util.Optional;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

@Slf4j
@Component
public class RssChecker {

    private static final String RSS_PARSING_ERROR_MESSAGE = "RSS문서를 파싱할 수 없습니다.";

    public Optional<RssFeedData> parseRssDocuments(CreateRequest request) {
        BlogPlatform blogPlatform = BlogPlatform.parseLink(request.blogUrl());

        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                    .parse(request.blogUrl());
            Element tag = (Element) document.getElementsByTagName("channel")
                    .item(0);

            return Optional.of(new RssFeedData(
                    getTagValue(tag, RssItemTagName.TITLE),
                    request.blogUrl(),
                    blogPlatform,
                    getTagValue(tag, RssItemTagName.DESCRIPTION)
            ));

        } catch (SAXException | IOException | ParserConfigurationException e) {
            log.error(RSS_PARSING_ERROR_MESSAGE);
            return Optional.empty();
        }
    }

    private String getTagValue(Node parentNode, RssItemTagName tagName) {
        Element element = (Element) parentNode;

        return element.getElementsByTagName(tagName.getTagName()).item(0).getTextContent();
    }

}
