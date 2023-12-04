package com.flytrap.rssreader.service.alert.platform;

import com.flytrap.rssreader.infrastructure.properties.SlackProperties;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class SlackAlarmService implements AlarmService {

    private final SlackProperties slackProperties;
    private final WebClient.Builder webClient;

    @Override
    public void notifyReturn() {
        StringBuilder sb = new StringBuilder();
        sb.append("새로운 글이 갱신되었습니다!");
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
                .toBodilessEntity()
                .block();
    }
}
