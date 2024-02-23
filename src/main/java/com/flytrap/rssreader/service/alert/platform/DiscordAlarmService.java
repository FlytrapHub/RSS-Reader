package com.flytrap.rssreader.service.alert.platform;

import com.flytrap.rssreader.domain.alert.AlertPlatform;
import com.flytrap.rssreader.service.dto.AlertParam;
import java.util.Map;
import java.util.Map.Entry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiscordAlarmService implements AlarmService {

    private static final int MAX_CONTENT_LENGTH = 2000;

    private final WebClient.Builder webClient;

    @Override
    public boolean isSupport(AlertPlatform alertPlatform) {

        return alertPlatform == AlertPlatform.DISCORD;
    }

    @Override
    public void sendAlert(AlertParam value) {
        StringBuilder sb = new StringBuilder();
        sb.append("*새로운 글이 갱신되었습니다!*\n\n");
        sb.append("*폴더 이름:* ").append(value.folderName()).append("\n\n");

        for (Entry<String, String> entry : value.posts().entrySet()) {
            String appendStr = "[" + entry.getValue() + "](" + entry.getKey() + ")\n\n";

            if (sb.length() + appendStr.length() > MAX_CONTENT_LENGTH) {
                break;
            }

            sb.append(appendStr);
        }

        send(value.webhookUrl(), sb.toString());
    }

    private void send(String webhookUrl, String message) {
        webClient
            .baseUrl(webhookUrl)
            .build()
            .method(HttpMethod.POST)
            .uri("")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(Map.of("content", message)))
            .retrieve()
            .bodyToMono(Void.class)
            .doOnSuccess(response -> log.info("Discord notification sent successfully. Response: {}", response))
            .doOnError(error -> log.error("Error sending Discord notification. Error: {}", error.getLocalizedMessage()))
            .block();
    }
}
