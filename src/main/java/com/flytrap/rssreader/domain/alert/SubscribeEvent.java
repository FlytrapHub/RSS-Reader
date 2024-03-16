package com.flytrap.rssreader.domain.alert;

import java.util.Map;

@Deprecated
public record SubscribeEvent(Long subscribeId, Map<String, String> posts) {

}
