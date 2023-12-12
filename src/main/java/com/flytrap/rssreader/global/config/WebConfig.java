package com.flytrap.rssreader.global.config;

import com.flytrap.rssreader.presentation.resolver.AuthorizationArgumentResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AuthorizationArgumentResolver authorizationArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authorizationArgumentResolver);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:5173", "http://venus-feeder-fe.s3-website.ap-northeast-2.amazonaws.com", "http://d3lxu674i34hip.cloudfront.net")
            .allowedMethods(
                HttpMethod.OPTIONS.name(), HttpMethod.GET.name(),
                HttpMethod.POST.name(), HttpMethod.PUT.name(),
                HttpMethod.PATCH.name(), HttpMethod.DELETE.name(),
                HttpMethod.HEAD.name())
            .allowedHeaders(
                HttpHeaders.AUTHORIZATION, HttpHeaders.CONTENT_TYPE)
            .exposedHeaders("Custom-Header")
            .allowCredentials(true)
            .maxAge(3600);
    }
}
