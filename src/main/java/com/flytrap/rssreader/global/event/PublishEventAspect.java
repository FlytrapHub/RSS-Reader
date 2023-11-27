package com.flytrap.rssreader.global.event;

import io.micrometer.common.util.StringUtils;
import java.lang.reflect.InvocationTargetException;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PublishEventAspect implements ApplicationEventPublisherAware {

    private final ExpressionParser expressionParser = new SpelExpressionParser();
    private final String spelRegex = "\\#\\{(.*)\\}";

    private ApplicationEventPublisher eventPublisher;

    /**
     * return값 없으면 new eventType()
     * params값 없으면 new eventType(returnValue)
     * params값이 문자열이면 new eventType(params)
     * params값이 SpEL이면 parse 후에 eventType(params)
     */
    @AfterReturning(pointcut = "pointcut(publishEvent)", returning = "returnValue", argNames = "publishEvent,returnValue")
    public void afterReturning(PublishEvent publishEvent, Object returnValue) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        Object event;

        if (returnValue == null) {
            event = publishEvent.eventType()
                    .getDeclaredConstructor()
                    .newInstance();

        } else if (StringUtils.isEmpty(publishEvent.params())) {
            event = publishEvent.eventType()
                    .getConstructor(returnValue.getClass())
                    .newInstance(returnValue);

        } else if (isSpel(publishEvent.params())) {
            String spel = publishEvent.params().replaceAll(spelRegex, "$1");
            Object constructArg = expressionParser.parseExpression(spel).getValue(returnValue);
            assert constructArg != null;
            event = publishEvent.eventType()
                    .getDeclaredConstructor(constructArg.getClass())
                    .newInstance(constructArg);

        } else {
            event = publishEvent.eventType()
                    .getConstructor(String.class)
                    .newInstance(publishEvent.params());
        }

        eventPublisher.publishEvent(event);
    }

    private boolean isSpel(String params) {
        Pattern spelPattern = Pattern.compile(spelRegex);
        return spelPattern.matcher(params).matches();
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
    }
}
