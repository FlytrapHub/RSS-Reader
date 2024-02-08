package com.flytrap.rssreader.infrastructure.api;

import com.flytrap.rssreader.domain.subscribe.BlogPlatform;
import com.flytrap.rssreader.infrastructure.api.dto.RssTag;
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
import org.xml.sax.SAXException;

@Slf4j
@Component
public class RssChecker implements RssDocumentParser {

    public Optional<RssFeedData> parseRssDocuments(CreateRequest request) {
        BlogPlatform blogPlatform = BlogPlatform.parseLink(request.blogUrl());

        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .parse(request.blogUrl());

            String rootTagName = document.getDocumentElement().getTagName();
            if (IS_RSS_ROOT_TAG.test(rootTagName)) {
                System.out.println("This document is an RSS feed.");

                return Optional.of(createFeedDataFromRss(document, request.blogUrl(), blogPlatform));
            } else if (IS_ATOM_ROOT_TAG.test(rootTagName)) {
                System.out.println("This document is an Atom feed.");

                return Optional.of(createFeedDataFromAtom(document, request.blogUrl(), blogPlatform));
            } else {
                throw new ParserConfigurationException();
            }

        } catch (SAXException | IOException | ParserConfigurationException e) {
            log.error(RSS_PARSING_ERROR_MESSAGE);
            return Optional.empty();
        }
    }

    private RssFeedData createFeedDataFromRss(Document document, String url,
        BlogPlatform blogPlatform) {
        Element tag = (Element) document.getElementsByTagName(
                RssTag.RssSubscribeTag.CHANNEL.getTagName())
            .item(0);

        return new RssFeedData(
            getTagValue(tag, RssTag.RssSubscribeTag.TITLE.getTagName()),
            url,
            blogPlatform,
            getTagValue(tag, RssTag.RssSubscribeTag.DESCRIPTION.getTagName())
        );
    }

    private RssFeedData createFeedDataFromAtom(Document document, String url,
        BlogPlatform blogPlatform) {
        Element tag = (Element) document.getElementsByTagName(
                RssTag.AtomSubscribeTag.FEED.getTagName())
            .item(0);

        return new RssFeedData(
            getTagValue(tag, RssTag.AtomSubscribeTag.TITLE.getTagName()),
            url,
            blogPlatform,
            ""
        );
    }

}
