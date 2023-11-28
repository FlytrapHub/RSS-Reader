package com.flytrap.rssreader.global.event;

import com.flytrap.rssreader.domain.post.PostOpenEvent;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PublishEvent {
    Class<? extends EventHolder> eventType();
    String params() default "";

}
