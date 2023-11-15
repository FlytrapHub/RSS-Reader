package com.flytrap.rssreader.infrastructure.api;

import com.flytrap.rssreader.infrastructure.api.dto.AccessToken;
import com.flytrap.rssreader.infrastructure.api.dto.UserResource;
import com.flytrap.rssreader.infrastructure.properties.OauthProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public record AuthGithubProvider(WebClient githubAuthorizationServer,
                                 WebClient githubResourceServer,
                                 WebClient githubResourceEmailServer,
                                 OauthProperties oauthProperties) implements AuthProvider {

    @Override
    public Mono<AccessToken> requestAccessToken(String code) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", code);
        formData.add("client_id", oauthProperties.github().clientId());
        formData.add("client_secret", oauthProperties.github().clientSecret());
        formData.add("redirect_uri", oauthProperties.github().redirectUri());

        return githubAuthorizationServer
            .post()
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData(formData))
            .retrieve()
            .onStatus(status -> status.is4xxClientError()
                    || status.is5xxServerError()
                , clientResponse ->
                    clientResponse.bodyToMono(String.class)
                        .map(body -> new Exception("exception"))) // TODO 외부 API 오류시 처리
            .bodyToMono(AccessToken.class);
    }

    @Override
    public Mono<UserResource> requestUserResource(AccessToken accessToken) {
        return githubResourceServer
            .get()
            .header(HttpHeaders.AUTHORIZATION, accessToken.getHeadValue())
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(status -> status.is4xxClientError()
                    || status.is5xxServerError()
                , clientResponse ->
                    clientResponse.bodyToMono(String.class)
                        .map(body -> new Exception("exception"))) // TODO 외부 API 오류시 처리
            .bodyToMono(UserResource.class);

    private Mono<List<UserEmailResource>> getEmailResource(AccessToken accessToken) {
        return githubResourceEmailServer
            .get()
            .header(HttpHeaders.AUTHORIZATION, accessToken.getHeadValue())
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(status -> status.is4xxClientError()
                            || status.is5xxServerError()
                    , clientResponse ->
                            clientResponse.bodyToMono(String.class)
                                    .map(body -> new Exception(
                                            "exception"))) // TODO 외부 API 오류시 처리
            .bodyToMono(new ParameterizedTypeReference<List<UserEmailResource>>() {});
    }

}
