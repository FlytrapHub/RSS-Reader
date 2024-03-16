package com.flytrap.rssreader.api.alert.infrastructure.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class AlertPlatformConverter implements AttributeConverter<AlertPlatform, Integer> {

    @Override
    public Integer convertToDatabaseColumn(AlertPlatform alertPlatform) {
        return alertPlatform.getValue();
    }

    @Override
    public AlertPlatform convertToEntityAttribute(Integer dbData) {
        return AlertPlatform.ofCode(dbData);
    }
}
