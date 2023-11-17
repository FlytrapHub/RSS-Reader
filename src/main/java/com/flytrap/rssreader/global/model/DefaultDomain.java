package com.flytrap.rssreader.global.model;

import java.io.Serializable;

public interface DefaultDomain {

    default String getDomainCode() {
        return this.getClass().getAnnotation(Domain.class).name();
    }
}
