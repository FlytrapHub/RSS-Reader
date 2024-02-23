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
public class SlackAlarmService implements AlarmService {

    private final WebClient.Builder webClient;

    @Override
    public boolean isSupport(AlertPlatform alertPlatform) {

        return alertPlatform == AlertPlatform.SLACK;
    }

    @Override
    public void sendAlert(AlertParam value) {
        StringBuilder sb = new StringBuilder();
        sb.append("*새로운 글이 갱신되었습니다!*\n\n");
        sb.append("*폴더 이름:* ").append(value.folderName()).append("\n\n");

        for (Entry<String, String> entry : value.posts().entrySet()) {
            sb.append("<").append(entry.getKey()).append("|").append(entry.getValue()).append(">\n\n");
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
                .body(BodyInserters.fromValue(Map.of("text", message)))
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> log.info("Slack notification sent successfully. Response: {}", response))
                .doOnError(error -> log.error("Error sending Slack notification. Error: {}", error.getMessage(), error))
                .block();
    }
}
