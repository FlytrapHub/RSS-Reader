package com.flytrap.rssreader.infrastructure.api;

import com.flytrap.rssreader.infrastructure.api.dto.RssItemResource;
import com.flytrap.rssreader.infrastructure.api.dto.RssItemTagName;
import com.flytrap.rssreader.infrastructure.entity.post.PostEntity;
import com.flytrap.rssreader.infrastructure.entity.subscribe.SubscribeEntity;
import com.flytrap.rssreader.infrastructure.repository.PostEntityJpaRepository;
import com.flytrap.rssreader.infrastructure.repository.SubscribeEntityJpaRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 구독한 블로그의 RSS 문서에서 게시글을 파싱해 수집하는 게시글 콜렉터
 */
@AllArgsConstructor
@Component
public class RssPostCollector {

    private static final int TEN_MINUTE = 600_000;

    private final SubscribeEntityJpaRepository subscribeEntityJpaRepository;
    private final PostEntityJpaRepository postEntityJpaRepository;

    @Transactional
    @Scheduled(fixedDelay = TEN_MINUTE)
    public void executeTask() throws ParserConfigurationException, IOException, SAXException {
        List<SubscribeEntity> subscribes = subscribeEntityJpaRepository.findAll();

        for (SubscribeEntity subscribe : subscribes) {
            List<RssItemResource> itemResources = collectRssPost(subscribe.getUrl());

            for (RssItemResource itemResource : itemResources) {
                PostEntity post = PostEntity.from(itemResource, subscribe);
                if (!postEntityJpaRepository.existsByGuidAndSubscribe(post.getGuid(), subscribe)) {
                    postEntityJpaRepository.saveAndFlush(post);
                }
            }
        }
    }

    private List<RssItemResource> collectRssPost(String url)
        throws ParserConfigurationException, IOException, SAXException {

        List<RssItemResource> itemResources = new ArrayList<>();

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

        return itemResources;
    }

    private String getTagValue(Node parentNode, RssItemTagName tagName) {
        Element element = (Element) parentNode;

        return element.getElementsByTagName(tagName.getTagName()).item(0).getTextContent();
    }
}
