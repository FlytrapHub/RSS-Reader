package com.flytrap.rssreader.infrastructure.api;

import com.flytrap.rssreader.infrastructure.api.dto.RssItemResource;
import com.flytrap.rssreader.infrastructure.api.dto.RssItemTagName;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

    public List<RssItemResource> parseRssDocument(String url) {

        List<RssItemResource> itemResources = new ArrayList<>();

        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url);
            NodeList itemList = document.getElementsByTagName("item");

            for (int i = 0; i < itemList.getLength(); i++) {
                Node node = itemList.item(i);

                itemResources.add(
                    new RssItemResource(
                        getTagValue(node, RssItemTagName.GUID),
                        getTagValue(node, RssItemTagName.TITLE),
                        getTagValue(node, RssItemTagName.DESCRIPTION),
                        getTagValue(node, RssItemTagName.PUB_DATE)
                    )
                );
            }
        } catch (SAXException | IOException | ParserConfigurationException e) {
            log.error(RSS_PARSING_ERROR_MESSAGE);
        }

        return itemResources;
    }

    private String getTagValue(Node parentNode, RssItemTagName tagName) {
        Element element = (Element) parentNode;

        return element.getElementsByTagName(tagName.getTagName()).item(0).getTextContent();
    }
}
