package com.flytrap.rssreader.api.alert.business.service.platform;

import com.flytrap.rssreader.infrastructure.properties.SlackProperties;
import com.flytrap.rssreader.service.dto.AlertParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SlackAlarmService implements AlarmService {

    private final SlackProperties slackProperties;
    private final WebClient.Builder webClient;

    @Override
    public void notifyReturn(AlertParam value) {
        StringBuilder sb = new StringBuilder();
        sb.append("*새로운 글이 갱신되었습니다!*\n\n");
        sb.append("*폴더 이름:* ").append(value.name()).append("\n\n");

        for (Map.Entry<String, String> entry : value.posts().entrySet()) {
            sb.append("<").append(entry.getKey()).append("|").append(entry.getValue()).append(">\n\n");
        }
        send(sb.toString());
    }



    private void send(String message) {
        webClient
                .baseUrl(slackProperties.webHookUrl())
                .build()
                .method(HttpMethod.POST)
                .uri("")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(Map.of("text", message)))
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> log.info("Slack notification sent successfully. Response: {}", response))
                .doOnError(error -> log.error("Error sending Slack notification. Error: {}", error.getMessage()))
                .block();
    }
}
