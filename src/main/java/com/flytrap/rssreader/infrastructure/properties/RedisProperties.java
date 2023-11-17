package com.flytrap.rssreader.infrastructure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "spring.data.redis")
public record RedisProperties(int port,
                              String host) {

    @ConstructorBinding
    public RedisProperties(int port, String host) {
        this.port = port;
        this.host = host;
    }
}
