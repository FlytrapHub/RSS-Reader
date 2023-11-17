package com.flytrap.rssreader.global.model;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.TYPE})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Domain {

    enum Type {
        DOMAIN,
        SUBDOMAIN
    }

    Type type() default Type.DOMAIN;
    String name();
}
