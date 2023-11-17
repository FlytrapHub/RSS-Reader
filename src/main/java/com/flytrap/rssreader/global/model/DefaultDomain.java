package com.flytrap.rssreader.global.model;

import java.io.Serializable;

public abstract class DefaultDomain implements Serializable {

    public String getDomainCode() {
        return this.getClass().getAnnotation(Domain.class).name();
    }
}
