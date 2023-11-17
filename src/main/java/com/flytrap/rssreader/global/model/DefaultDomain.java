package com.flytrap.rssreader.global.model;

import java.io.Serializable;

public abstract class DefaultDomain implements Serializable {

    public abstract Long getId();

    public String getDomainCode() {
        return this.getClass().getAnnotation(Domain.class).name();
    }

    public String getDomainCodeWithId() {
        return String.format("%s_%d", this.getDomainCode(), this.getId());
    }

}
