package com.flytrap.rssreader.config;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationPropertiesScan("com.flytrap.rssreader.infrastructure.properties")
public class PropertiesScanConfig {
}
