package com.flytrap.rssreader.infrastructure.api;

import com.flytrap.rssreader.global.exception.NoSuchDomainException;
import com.flytrap.rssreader.infrastructure.api.dto.RssItemTagName;
import com.flytrap.rssreader.infrastructure.api.dto.RssSubscribeResource;
import com.flytrap.rssreader.infrastructure.api.dto.RssSubscribeResource.RssItemResource;
import com.flytrap.rssreader.infrastructure.api.dto.RssSubscribeTagName;
import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 구독한 블로그의 RSS 문서에서 게시글을 파싱해 그 결과를 반환하는 RSS 게시글 파서
 */
@Slf4j
@NoArgsConstructor
@Component
public class RssPostParser {

    private static final String RSS_PARSING_ERROR_MESSAGE = "RSS문서를 파싱할 수 없습니다.";

    public Optional<RssSubscribeResource> parseRssDocuments(String url) {

        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .parse(url);

            String subscribeTitle = extractSubscribeTitle(document);
            List<RssItemResource> itemResources = extractItemResources(document);

            return Optional.of(new RssSubscribeResource(subscribeTitle, itemResources));
        } catch (SAXException | IOException | ParserConfigurationException e) {
            log.error(RSS_PARSING_ERROR_MESSAGE);
            return Optional.empty();
        }
    }

    private String extractSubscribeTitle(Document document) {
        Element chanel = (Element) document.getElementsByTagName(
            RssSubscribeTagName.CHANNEL.getTagName()).item(0);

        return chanel.getElementsByTagName(RssSubscribeTagName.TITLE.getTagName()).item(0)
            .getTextContent();
    }

    private List<RssItemResource> extractItemResources(Document document) {
        List<RssItemResource> itemResources = new ArrayList<>();

        NodeList itemList = document.getElementsByTagName("item");

        for (int i = 0; i < itemList.getLength(); i++) {
            Node node = itemList.item(i);

            itemResources.add(
                new RssItemResource(
                    getTagValue(node, RssItemTagName.GUID),
                    getTagValue(node, RssItemTagName.TITLE),
                    getTagValue(node, RssItemTagName.DESCRIPTION),
                    convertToInstant(getTagValue(node, RssItemTagName.PUB_DATE))
                )
            );
        }

        return itemResources;
    }


    private String getTagValue(Node parentNode, RssItemTagName tagName) {
        Element element = (Element) parentNode;

        return element.getElementsByTagName(tagName.getTagName()).item(0).getTextContent();
    }

    private Instant convertToInstant(String parsingDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z",
            Locale.ENGLISH);

        return Instant.from(formatter.parse(parsingDate));
    }

}
