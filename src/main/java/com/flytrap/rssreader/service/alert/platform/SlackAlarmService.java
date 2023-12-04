package com.flytrap.rssreader.service.alert.platform;

import com.flytrap.rssreader.infrastructure.properties.SlackProperties;
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

    private final SlackProperties slackProperties;
    private final WebClient.Builder webClient;

    @Override
    public void notifyReturn(AlertParam value) {
        StringBuilder sb = new StringBuilder();
        sb.append("새로운 글이 갱신되었습니다!\n\n");
        sb.append("폴더 이름 :").append(value.name()).append("\n\n");

        for (Entry<String, String> entry : value.posts().entrySet()) {
            sb.append("주소 :").append(entry.getKey()).append("\n\n")
                    .append("제목 :").append(entry.getValue()).append("\n\n");
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
                .toBodilessEntity()
                .block();
    }
}
